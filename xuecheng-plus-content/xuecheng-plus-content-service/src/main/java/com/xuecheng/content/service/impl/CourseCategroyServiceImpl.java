package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategroyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Starry
 * @version 1.0
 * @description TODO
 * @date 2023/3/8 16:14
 */
@Slf4j
@Service
public class CourseCategroyServiceImpl implements CourseCategroyService {
    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        //得到了根节点下边所有节点
        List<CourseCategoryTreeDto> categoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);
        //定义一个list作为最终返回数据
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = new ArrayList<>();
        //为了找打父节点，定义map
        HashMap<String,CourseCategoryTreeDto> nodeMap = new HashMap<>();
        //将数据封装到list，只包括根节点下属节点
        categoryTreeDtos.stream().forEach(item ->{
            nodeMap.put(item.getId(),item);
            if(item.getParentid().equals(id)){
                courseCategoryTreeDtos.add(item);
            }
            //找到该节点的父节点
            String parentId = item.getParentid();
            //找到父节点对象
            CourseCategoryTreeDto parentNode = nodeMap.get(parentId);
            if (parentNode != null){
                List childrenTreeNodes = parentNode.getChildrenTreeNodes();
                if (childrenTreeNodes == null){
                    parentNode.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                //找到子节点，放到父节点的childrenTreeNodes属性中
                parentNode.getChildrenTreeNodes().add(item);
            }
        });
        //返回的list中只包括根节点的下属节点
        return courseCategoryTreeDtos;
    }
}
