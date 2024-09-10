package me.emmy.practice.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArenaType {
	STANDALONE,
	SHARED,
	DUPLICATE
}
