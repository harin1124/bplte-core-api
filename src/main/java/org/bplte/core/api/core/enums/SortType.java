package org.bplte.core.api.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
	ASC("ASC", "Ascending"),
	DESC("DESC", "Descending");

	private final String name;
	private final String value;
}
