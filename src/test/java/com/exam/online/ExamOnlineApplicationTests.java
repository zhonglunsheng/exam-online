package com.exam.online;

import com.exam.online.entity.Paper;
import com.exam.online.mapper.PaperMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamOnlineApplicationTests {

    @Autowired
    PaperMapper paperMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<Paper> userList = paperMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}

