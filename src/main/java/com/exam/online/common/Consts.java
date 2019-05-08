package com.exam.online.common;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-11 18:59
 */
public class Consts {
    public final static String SALT = "exam";

    public interface Common {
        String ARGUS_NULL = "传入参数为空";
        String SUCCESS = "操作成功";
        String ERROR = "操作失败";
        String STUDENT_TOKEN = "s_token";
        String USER_TOKEN = "u_token";
    }

    public interface Login {
        String LOGIN_NULL = "账号或密码为空";
    }

    public interface Question {
        String[] TYPENAME = new String[]{"单选题","多选题","判断题","简答题","应用题"};
        String QUESTION_TITLE_NULL = "请传入题目名称";
        String QUESTION_ANSWER_NULL = "请传入题目参考答案";
        String[] TYPE_NAME = new String[]{"单选题", "多选题", "判断题", "简答题", "应用题"};

    }

    public interface Paper {
        String PAPER_ID_NULL = "传入的试卷ID为空";
        /**
         * 已发布
         */
        String STATUS_CD_SUCCESS = "0";
        /**
         * 未发布
         */
        String STATUS_CD_FAILURE = "1";
    }

    public interface Query {
        String CREATE_TIME = "createTime";
        String ASC = "asc";
        String USER_ID = "userId";
    }

    public interface Operate {
        String SELECT = "select";
    }

    public interface Submit {
        Integer BLANK_SUBMIT = 2;
    }
}
