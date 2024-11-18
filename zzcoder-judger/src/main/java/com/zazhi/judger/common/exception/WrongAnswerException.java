package com.zazhi.judger.common.exception;

public class WrongAnswerException extends JudgeException {
    public WrongAnswerException() {
        super("Wrong Answer", 1004);
    }
    public WrongAnswerException(String details) {
        super("Wrong Answer", 1004, details);
    }
}
