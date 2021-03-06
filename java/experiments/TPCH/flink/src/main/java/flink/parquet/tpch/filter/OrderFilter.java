/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package flink.parquet.tpch.filter;

import parquet.column.ColumnReader;
import parquet.filter.ColumnPredicates;
import parquet.filter.ColumnRecordFilter;
import parquet.filter.RecordFilter;
import parquet.filter.UnboundRecordFilter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OrderFilter implements UnboundRecordFilter {

	public class BeforeDate implements ColumnPredicates.PredicateFunction<String> {

		@Override
		public boolean functionToApply(String input) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try {
				date = format.parse("1995-03-12");
				return format.parse(input).before(date);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	public RecordFilter bind(Iterable<ColumnReader> readers) {

		return ColumnRecordFilter.column(
			"ORDERDATE",
			ColumnPredicates.applyFunctionToString(new BeforeDate())
		).bind(readers);
	}
}
