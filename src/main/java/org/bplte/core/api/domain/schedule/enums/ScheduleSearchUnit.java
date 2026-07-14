package org.bplte.core.api.domain.schedule.enums;

import java.time.LocalDate;

/**
 * 스케줄 검색 단위
 */
public enum ScheduleSearchUnit {
	YEAR {
		@Override
		public LocalDate rangeStart(LocalDate referenceDate) {
			return referenceDate.withDayOfYear(1);
		}

		@Override
		public LocalDate rangeEnd(LocalDate referenceDate) {
			return referenceDate.withDayOfYear(referenceDate.lengthOfYear());
		}
	},
	MONTH {
		@Override
		public LocalDate rangeStart(LocalDate referenceDate) {
			return referenceDate.withDayOfMonth(1);
		}

		@Override
		public LocalDate rangeEnd(LocalDate referenceDate) {
			return referenceDate.withDayOfMonth(referenceDate.lengthOfMonth());
		}
	},
	DAY {
		@Override
		public LocalDate rangeStart(LocalDate referenceDate) {
			return referenceDate;
		}

		@Override
		public LocalDate rangeEnd(LocalDate referenceDate) {
			return referenceDate;
		}
	};

	/** referenceDate 기준 검색 시작일 */
	public abstract LocalDate rangeStart(LocalDate referenceDate);

	/** referenceDate 기준 검색 종료일 */
	public abstract LocalDate rangeEnd(LocalDate referenceDate);
}
