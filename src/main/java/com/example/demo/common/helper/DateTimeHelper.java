package com.example.demo.common.helper;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DateTimeHelper {

	public static List<LocalTime> generateLocalTimeInterval(LocalTime start, LocalTime end) {
		long count = Duration.between(start, end).dividedBy(Duration.ofHours(1));
		// TODO
		return List.of(
			LocalTime.of(12, 0),
			LocalTime.of(13, 0),
			LocalTime.of(14, 0),
			LocalTime.of(15, 0),
			LocalTime.of(16, 0),
			LocalTime.of(17, 0),
			LocalTime.of(18, 0),
			LocalTime.of(19, 0),
			LocalTime.of(20, 0)
		);
	}

}
