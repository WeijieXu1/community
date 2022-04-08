package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PseudoColumnUsage;

/**
 * @author xuweijie
 *@create 2022-03-20 19:59
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
    private static final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;
    @Value("${qiniu.key.access}")
    private String accessKey;
    @Value("${qiniu.key.secret}")
    private String secretKey;
    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;
    @Value("${qiniu.bucket.header.url}")
    private String headerBucketUrl;


    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(Model model){
        //上传文件名称
        String fileName = CommunityUtil.generateUUID();
        //设置响应信息
        StringMap policy=new StringMap();
        policy.put("returnBody",CommunityUtil.getJSONString(0));
        //生成上传凭证
        Auth auth=Auth.create(accessKey,secretKey);
        String uploadToken=auth.uploadToken(headerBucketName,fileName,3600,policy);
        model.addAttribute("uploadToken",uploadToken);
        model.addAttribute("fileName",fileName);

        return "/site/setting";
    }
    //更新头像路径
    @RequestMapping(path = "header/url",method = RequestMethod.POST)
    @ResponseBody
    public String updateHeaderUrl(String fileName){
        if(StringUtils.isBlank(fileName)){
            CommunityUtil.getJSONString(1,"文件名不能为空");

        }
        String url=headerBucketUrl+"/"+fileName;
        userService.updateHeader(hostHolder.getUser().getId(),url);
        return CommunityUtil.getJSONString(0);
    }


    //废弃
    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage,Model model){
        if(headerImage==null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }
        //生成随机文件名
        fileName=CommunityUtil.generateUUID()+suffix;
        //确定文件存放的路径
        File dest=new File(uploadPath+"/"+fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败"+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常",e);
        }
        //更新当前用户头像路径（web访问路径）
        User user = hostHolder.getUser();
        String headerUrl=domain+contextPath+"/user/header/"+fileName;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";

    }
    //废弃
    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //服务器路径
        fileName=uploadPath+"/"+fileName;
        //文件后缀
        String suffix=fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/"+suffix);
        try(
                FileInputStream fis = new FileInputStream(fileName);
                ) {
            ServletOutputStream os = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int b=0;
            while((b=fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }

        } catch (IOException e) {
            logger.error("读取头像失败"+e.getMessage());

        }
    }
    @LoginRequired
    @RequestMapping(path = "/update",method = RequestMethod.POST)
    public String updatePassword(Model model, String oldPassword, String newPassword, String repeatNewPassword,
                                 @CookieValue("ticket") String ticket, HttpSession session){
        if(StringUtils.isBlank(oldPassword)){
            model.addAttribute("oldPasswordMsg","密码不能为空");
            return "/site/setting";

        }

        User user = hostHolder.getUser();
        oldPassword=CommunityUtil.md5(oldPassword+user.getSalt());
        if(!user.getPassword().equals(oldPassword)){
            model.addAttribute("oldPasswordMsg","原始密码错误");
            return "/site/setting";

        }
        if(StringUtils.isBlank(newPassword)){
            model.addAttribute("newPasswordMsg","请输入修改后的密码");
            return "/site/setting";

        }
        if(!StringUtils.isBlank(repeatNewPassword)&&!repeatNewPassword.equals(newPassword)){
            model.addAttribute("newPasswordMsg","输入的密码不一致");
            return "/site/setting";
        }
        newPassword=CommunityUtil.md5(newPassword+user.getSalt());
        userService.updatePassword(user.getId(),newPassword);
        userService.logout(ticket);
        //session.setAttribute("passwordMsg","修改成功，请重新登录！");
//        model.addAttribute("usernameMsg","修改成功，请重新登录！");
        return "redirect:/login";
    }
    //个人主页
    @RequestMapping(path = "/profile/{userId}",method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId,Model model){
        User user = userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("该用户不存在！");
        }
        //用户基本信息
        model.addAttribute("user",user);
        //获赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        //关注数量
        long followeeCount = followService.findFolloweecount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount",followerCount);


        //对所看实体是否已关注
        boolean hasFollowed=false;
        if(hostHolder.getUser()!=null){
            hasFollowed=followService.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER,userId);
        }
        model.addAttribute("hasFollowed",hasFollowed);


        model.addAttribute("likeCount",likeCount);
        return "/site/profile";

    }

}













































































