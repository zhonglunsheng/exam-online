package com.exam.online.common;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-11 18:59
 */
public class Consts {
    public static final String SALT = "exam";
    public interface COMOON{
        public static final String ARGUS_NULL = "传入参数为空";
    }
    public interface LOGIN{
        public static final String LOGIN_NULL = "账号或密码为空";
    }
    public interface QUESTION{
        public static final String QUESTION_TITLE_NULL = "请传入题目名称";
        public static final String QUESTION_ANSWER_NULL = "请传入题目参考答案";

    }
    public interface PAPER{
        public static final String PAPER_ID_NULL = "传入的试卷ID为空";
    }
}
