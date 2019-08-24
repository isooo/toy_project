package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;

import java.util.*;

public class AmountMenu implements Menu {
    public static final String Command = "환전 금액 계산하기";

    @Override
    public List<Message> getMessages(String userId, String userMessage) {
        // TODO : 해당 user의 최근 조회 CurrnecyRate 가져오기

        // TODO : base currency의 unit을 아래 괄호 사이에 넣어줄 것
        return Collections.singletonList(new TextMessage("환전할 금액(" + "" + ")를 숫자로 입력해주세요."));
    }
}
