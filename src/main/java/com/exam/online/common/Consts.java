package com.exam.online.common;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-11 18:59
 */
public class Consts {
    public static final String SALT = "exam";

    public interface Common {
        public static final String ARGUS_NULL = "传入参数为空";
        public static final String SUCCESS = "操作成功";
        public static final String ERROR = "操作失败";
        String STUDENT_TOKEN = "s_token";
        String USER_TOKEN = "u_token";
    }

    public interface Login {
        public static final String LOGIN_NULL = "账号或密码为空";
    }

    public interface Question {
        String[] TYPENAME = new String[]{"单选题","多选题","判断题","简答题","应用题"};
        public static final String QUESTION_TITLE_NULL = "请传入题目名称";
        public static final String QUESTION_ANSWER_NULL = "请传入题目参考答案";

    }

    public interface Paper {
        public static final String PAPER_ID_NULL = "传入的试卷ID为空";
    }

    public interface Query {
        public static final String CREATE_TIME = "createTime";
        public static final String ASC = "asc";
        public static final String USER_ID = "userId";
    }

    public interface Operate {
        public static final String SELECT = "select";
    }

    public interface Submit {
        Integer BLANK_SUBMIT = 2;
    }
}
