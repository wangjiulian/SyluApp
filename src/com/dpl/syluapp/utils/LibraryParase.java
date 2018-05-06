package com.dpl.syluapp.utils;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dpl.syluapp.model.LibrarySelectInfo;

public class LibraryParase {

	private List<LibrarySelectInfo> listInfo = new ArrayList<LibrarySelectInfo>();

	public List<LibrarySelectInfo> parase(String library) {

		try {

			Document doc = Jsoup.parse(library);
			Elements elements1 = doc.select("table");

			Elements elements2 = elements1.select("tr");
			for (int i = 1; i < elements2.size(); i++) {
				String[] result22 = null;
				LibrarySelectInfo book = new LibrarySelectInfo();
				Elements code = elements2.get(i).select(
						"input[class=btn btn-success]");

				result22 = code.attr("onclick").split(",");
				String c = result22[1].substring(1, 9);
				System.out.println("-----" + c);
				book.setCode(c);
				Elements elements3 = elements2.get(i).select("td");

				book.setBbar_code(elements3.get(0).text());
				book.setbName(elements3.get(1).text());
				String info1 = elements3.get(1).text();
				book.setbName(info1.split("/")[0]);
				book.setbAuthor(StringUtil.FilterAuthor(info1.split("/")[1]));
				book.setbBorrowDay(elements3.get(2).text());
				book.setbReturnDay(elements3.get(3).text());
				book.setbRenew(elements3.get(4).text());

				listInfo.add(book);

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		for (LibrarySelectInfo table : listInfo) {
			System.out.println("booMessage--->" + table.toString());

		}
		return listInfo;
	}

}
