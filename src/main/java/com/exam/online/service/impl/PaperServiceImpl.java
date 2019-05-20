package com.exam.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exam.online.bo.PageResult;
import com.exam.online.common.Consts;
import com.exam.online.entity.Page;
import com.exam.online.entity.Paper;
import com.exam.online.mapper.PaperMapper;
import com.exam.online.service.PaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.online.util.CommonUtil;
import com.exam.online.util.DateTimeUtil;
import com.exam.online.util.ParamCheck;
import com.exam.online.vo.PaperVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.exam.online.common.Consts.Question.TYPENAME;

/**
 * <p>
 * 试卷信息表 服务实现类
 * </p>
 *
 * @author zhonglunsheng
 * @since 2019-02-11
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {

    @Autowired
    private PaperMapper paperMapper;

    @Override
    public Paper getPaperInfoForEdit(Integer id) {
        Paper paper = null;
        StringBuffer typeBuff = new StringBuffer();
        paper = paperMapper.selectById(id);
        paper.setStatusCd(Consts.Paper.STATUS_CD_FAILURE);
        paperMapper.updateById(paper);
        String[] typeName = paper.getType().split(",");
        for (int i = 0; i < TYPENAME.length; i++) {
            if (Integer.parseInt(typeName[i]) != 0){
                typeBuff.append(TYPENAME[i]);
            }
        }

        paper.setType(typeBuff.toString());
        return paper;
    }

    @Override
    public PageResult getPaperList(QueryWrapper queryWrapper, IPage page) {
        List<Paper> papers = paperMapper.selectList(null);
        page = paperMapper.selectPage(page, queryWrapper);
        List<Paper> records = page.getRecords();
        return PageResult.success(ParamCheck.getListNotNullSize(papers), getPaperVoList(records));
    }

    @Override
    public void paperSaveOrUpdate(Paper paper) {
        paper.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        // 字段拆分转换
        String[] scores = paper.getScore().split(",");
        String[] typeNums = paper.getTypeNums().split(",");
        String[] types = paper.getType().split(",");
        if (paper.getId() == null){
            paper.setCreateTime(DateTimeUtil.dateToStr(new Date()));
            paper.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        }else{
            paper.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        }

        int[] typeInt = new int[5];
        int[] scoreInt = new int[5];
        int[] typeNumsInt = new int[5];
        int total = 0;
        for (int i = 0; i < types.length; i++) {
            switch (types[i]) {
                case "单选题":
                    typeInt[0] = 1;
                    scoreInt[0] = Integer.parseInt(scores[0]);
                    typeNumsInt[0] = Integer.parseInt(typeNums[0]);
                    total += scoreInt[0] * typeNumsInt[0];
                    break;
                case "多选题":
                    typeInt[1] = 1;
                    scoreInt[1] = Integer.parseInt(scores[1]);
                    typeNumsInt[1] = Integer.parseInt(typeNums[1]);
                    total += scoreInt[1] * typeNumsInt[1];
                    break;
                case "判断题":
                    typeInt[2] = 1;
                    scoreInt[2] = Integer.parseInt(scores[2]);
                    typeNumsInt[2] = Integer.parseInt(typeNums[2]);
                    total += scoreInt[2] * typeNumsInt[2];
                    break;
                case "简答题":
                    typeInt[3] = 1;
                    scoreInt[3] = Integer.parseInt(scores[3]);
                    typeNumsInt[3] = Integer.parseInt(typeNums[3]);
                    total += scoreInt[3] * typeNumsInt[3];
                    break;
                case "填空题":
                    typeInt[4] = 1;
                    scoreInt[4] = Integer.parseInt(scores[4]);
                    typeNumsInt[4] = Integer.parseInt(typeNums[4]);
                    total += scoreInt[4] * typeNumsInt[4];
                    break;
                default:
            }
        }
        String type = CommonUtil.arrayForIntToStr(typeInt);
        String score = CommonUtil.arrayForIntToStr(scoreInt);
        String typeNum = CommonUtil.arrayForIntToStr(typeNumsInt);
        paper.setType(type);
        paper.setScore(score);
        paper.setTypeNums(typeNum);
        saveOrUpdate(paper);
    }

    /**
     * paperVo生成
     * @param records
     * @return
     */
    private List<PaperVo> getPaperVoList(List<Paper> records) {
        PaperVo paperVo;
        List<PaperVo> rows = new ArrayList<>();
        if (records != null) {
            for (Paper s :
                    records) {
                StringBuffer typeName = new StringBuffer();
                StringBuffer typeNum = new StringBuffer();
                StringBuffer scoreBuff = new StringBuffer();
                String[] types = new String[5];
                String[] typeNums = new String[5];
                String[] scores = new String[5];
                Integer totalScore = 0;
                paperVo = new PaperVo();
                BeanUtils.copyProperties(s, paperVo);

                String type = s.getType();
                String nums = s.getTypeNums();
                String score = s.getScore();
                if (!StringUtils.isBlank(type)) {
                    types = type.split(",");
                    typeNums = nums.split(",");
                    scores = score.split(",");
                }
                for (int i = 0; i < types.length; i++) {
                    if ("1".equals(types[i])) {
                        switch (i) {
                            case 0:
                                typeName.append("单选 ");
                                typeNum.append(typeNums[i] + " ");
                                scoreBuff.append(scores[i] + " ");
                                totalScore += Integer.parseInt(typeNums[i]) * Integer.parseInt(scores[i]);
                                break;
                            case 1:
                                typeName.append("多选 ");
                                typeNum.append(typeNums[i] + " ");
                                scoreBuff.append(scores[i] + " ");
                                totalScore += Integer.parseInt(typeNums[i]) * Integer.parseInt(scores[i]);
                                break;
                            case 2:
                                typeName.append("判断 ");
                                typeNum.append(typeNums[i] + " ");
                                scoreBuff.append(scores[i] + " ");
                                totalScore += Integer.parseInt(typeNums[i]) * Integer.parseInt(scores[i]);
                                break;
                            case 3:
                                typeName.append("简答 ");
                                scoreBuff.append(scores[i] + " ");
                                typeNum.append(typeNums[i] + " ");
                                totalScore += Integer.parseInt(typeNums[i]) * Integer.parseInt(scores[i]);
                                break;
                            case 4:
                                typeName.append("填空 ");
                                scoreBuff.append(scores[i] + " ");
                                typeNum.append(typeNums[i] + " ");
                                totalScore += Integer.parseInt(typeNums[i]) * Integer.parseInt(scores[i]);
                                break;
                            default:
                        }
                    }
                }
                paperVo.setTypeName(typeName.toString());
                paperVo.setTypeNums(typeNum.toString());
                paperVo.setScore(scoreBuff.toString());
                paperVo.setTotalScore(totalScore);
                paperVo.setStatus("0".equals(s.getStatusCd()) ? "已发布":"未发布");
                rows.add(paperVo);
            }
        }
        return rows;
    }
}
