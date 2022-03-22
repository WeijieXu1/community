package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuweijie
 * @create 2022-03-21 18:28
 */
@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符号
    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode root = new TrieNode();

    @PostConstruct
    public void init() {

        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while((keyword=reader.readLine())!=null){
                //添加到前缀树
                this.addKeyword(keyword);
            }

        } catch (IOException e) {
            logger.error("加载敏感词文件失败" + e.getMessage());
        }


    }
    //将敏感词添加到前缀树
    private void addKeyword(String keyword) {
        TrieNode temp=root;
        for (int i = 0; i <keyword.length() ; i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = temp.getSubNode(c);
            if(subNode==null){
                subNode=new TrieNode();
                temp.addSubNode(c,subNode);
            }
            //指向子节点 进入下一层循环
            temp=subNode;
            //设置结束标识
            if(i==keyword.length()-1){
                temp.setKeywordEnd(true);
            }

        }
    }

    //过滤敏感词
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //树指针
        TrieNode tempNode=root;
        //文本指针
        int begin=0;
        int end=0;

        StringBuilder sb = new StringBuilder();
        while(begin<text.length()){
            char c=text.charAt(end);
            if(isSymble(c)){
                //若指针一处于空节点
                if(tempNode==null){
                    sb.append(c);
                    begin++;
                }
                if(end==text.length()-1){
                    sb.append(c);
                    begin++;
                    end=begin;
                    tempNode=root;
                }else{
                    end++;
                }



                continue;

            }
            tempNode = tempNode.getSubNode(c);
            if(tempNode==null){
                sb.append(text.charAt(begin));
                begin++;
                end=begin;
                tempNode=root;
            }else if(tempNode.isKeywordEnd()){
                sb.append(REPLACEMENT);
                end++;
                begin=end;
                tempNode=root;
            }else{
                if(end>=text.length()-1){
                    begin++;
                    end=begin;
                    tempNode=root;
                }
                end++;
            }

        }
        sb.append(text.substring(begin));
        return sb.toString();

    }
    //判断是否为符号
    private boolean isSymble(char c ){
        //c<0x2E80||c>0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }




    //前缀树
    private class TrieNode {
        //关键词结束标识
        private boolean isKeywordEnd = false;
        //子节点
        private Map<Character, TrieNode> subnodes = new HashMap<>();


        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subnodes.put(c, node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c) {
            return subnodes.get(c);
        }
    }
}
