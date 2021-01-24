package com.soongjamm.football_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlayerPrinter {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired(required = false)
    @Qualifier("localized-full")
    DateTimeFormatter formatter;

    public PlayerPrinter() {
        formatter = DateTimeFormatter.ISO_DATE;
    }

    public void print() {
        List<Player> players = playerRepository.getPlayers();
        System.out.println("--선수 목록 출력--");
        for (Player player : players) {
            System.out.printf("이름 : %s  출생 : %s\n", player.getName(), formatter.format(player.getBirth()));
        }
    }

}
