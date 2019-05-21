package com.exam.online.message;

import com.exam.online.entity.Record;
import com.exam.online.entity.Score;
import com.exam.online.service.RecordService;
import com.exam.online.service.ScoreService;
import com.exam.online.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-05-21 14:46
 */
@Slf4j
@Component
public class RabbitMqReceive {


    @Autowired
    private RecordService recordService;

    @Autowired
    private ScoreService scoreService;

    /**
     * 自动创建队列
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue("myQueue"))
    public void processByDeclare(String message){
        // 将消息字符串 转换为map
        Map<String, Object> receiveMap= JsonUtil.string2Obj(message, Map.class);
        List<Score> commitScore = (List<Score>) receiveMap.get("commitScore");
        List<Record> records = (List<Record>) receiveMap.get("record");
        if (commitScore != null && records != null && records.size() != 0){
            recordService.saveBatch(records);
            scoreService.saveBatch(commitScore);
        }
    }

}
