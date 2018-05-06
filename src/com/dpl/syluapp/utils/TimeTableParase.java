package com.dpl.syluapp.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dpl.syluapp.db.TimeTableDb;
import com.dpl.syluapp.model.TimeTableInfo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class TimeTableParase {

	private TimeTableDb tableDb;
	private Context context;
	private WeekGet weekGet;
	String path = Environment.getExternalStorageDirectory() + "/";
	int i = 0, j = 0;
	List<TimeTableInfo> list = new ArrayList<TimeTableInfo>();

	public TimeTableParase(Context context) {

		this.context = context;
		tableDb = new TimeTableDb(context);
		weekGet = new WeekGet();
	}

	public List<TimeTableInfo> reads() {
		System.out.println("开始读取");

		File input = new File(path + "syluapp/sourse.txt");
		try {
			Object tb[], tb1[];
			int nclos = 0;
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.jb51.net/");
			Elements rowElements = doc.select("table[id=Table1]");

			Elements elements = rowElements.select("tr");
			for (j = 2; j < elements.size(); j++) {

				if (j % 2 == 0) {
					nclos++;
					Elements element = elements.get(j).select(
							"td[align=Center]");
					for (int d = 0; d < element.size(); d++)

					{
						// 分割 内容,
						String[] result = element.get(d).text().split(" ");
						System.out
								.println("-----result.length" + result.length);
						if (result.length == 4 || result.length == 5) {
							tb = new Object[7];

							tb[0] = result[0].toString();
							tb[2] = result[3].toString();
							tb[1] = result[2].toString();
							tb[5] = String.valueOf(nclos - 1);
							tb[6] = String.valueOf(d);
							String week[] = weekGet.getWeek(result[1]
									.toString());
							tb[3] = week[0];
							tb[4] = week[1];
							boolean flag = false;
							flag = tableDb.addTimeTable(tb);
							Log.i("TAG", "插入状态  ---->" + flag);

							System.out.println(result.length);
							TimeTableInfo course = new TimeTableInfo();
							course.setSubject(result[0].toString());
							course.setAddress(result[3].toString());
							list.add(course);

						}

						else if (result.length == 1) {

							TimeTableInfo course = new TimeTableInfo();
							course.setSubject("");
							course.setAddress("");
						//	list.add(course);

						} else {

							tb = new Object[7];
							tb[0] = result[0].toString();
							tb[2] = result[3].toString();
							tb[1] = result[2].toString();
							tb[5] = String.valueOf(nclos - 1);
							tb[6] = String.valueOf(d);

							String week[] = weekGet.getWeek(result[1]
									.toString());
							tb[3] = week[0];
							tb[4] = week[1];
							boolean flag = false;
							flag = tableDb.addTimeTable(tb);
							Log.i("TAG", "插入状态  ---->" + flag);
							TimeTableInfo course = new TimeTableInfo();
							course.setSubject(result[0].toString());
							course.setAddress(result[3].toString());
							list.add(course);

							String re = result[4].substring(1, 2);
							if ("调".equals(re)) {
								System.out.println("------调---"
										+ result[8].toString());
								tb1 = new Object[7];
								tb1[0] = result[5].toString();// 5
								tb1[2] = result[8].toString();// 7
								tb1[1] = result[7].toString();// 6
								tb1[5] = String.valueOf(nclos - 1);
								tb1[6] = String.valueOf(d);
								String week1[] = weekGet.getWeek(result[6]
										.toString());
								tb1[3] = week1[0];
								tb1[4] = week1[1];
								boolean flag1 = false;
								flag1 = tableDb.addTimeTable(tb1);
								TimeTableInfo course1 = new TimeTableInfo();
								course1.setSubject(result[5].toString());
								course1.setAddress(result[8].toString());
								list.add(course1);

							}

							else {
								System.out.println("------不调---"
										+ result[7].toString());
								tb = new Object[7];
								tb[0] = result[4].toString();// 5
								tb[2] = result[7].toString();// 7
								tb[1] = result[6].toString();// 6
								tb[5] = String.valueOf(nclos - 1);
								tb[6] = String.valueOf(d);
								String week1[] = weekGet.getWeek(result[5]
										.toString());
								tb[3] = week1[0];
								tb[4] = week1[1];
								boolean flag1 = false;
								flag1 = tableDb.addTimeTable(tb);
								TimeTableInfo course1 = new TimeTableInfo();
								course1.setSubject(result[4].toString());
								course1.setAddress(result[7].toString());
								list.add(course1);

							}

						}

					}
				}

			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		for (TimeTableInfo l : list) {

			System.out.println("ScheduleTable-->" + l.getCourse());
		}

		return list;
	}
}
