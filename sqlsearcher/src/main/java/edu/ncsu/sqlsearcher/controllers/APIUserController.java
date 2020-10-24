package edu.ncsu.sqlsearcher.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;



@RestController
public class APIUserController extends APIController {

    @GetMapping ( BASE_PATH + "/myId" )
    public String getSessionID () {
        return String
                .valueOf( Math.abs( RequestContextHolder.currentRequestAttributes().getSessionId().hashCode() / 100 ) );
    }
    
    
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

	@RequestMapping(value = "/pdfreport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> citiesReport() {

		try {
			ByteArrayInputStream bis = generatePDF();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=sqlproblems.pdf");
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bis));
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	static private ByteArrayInputStream generatePDF()
			throws DocumentException, MalformedURLException, IOException, URISyntaxException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final Document document = new Document();

		PdfWriter.getInstance(document, out);

		document.open();
		Paragraph chunk1 = new Paragraph(
				"Please work on the problems in the order listed in this handout.  Problems start on the next page, and some problems may be split across two pages.");

		document.add(chunk1);
		document.newPage();

		List<Chapter> problems = Arrays.asList(new Chapter[] { getProblem1(), getProblem2(), getProblem3(),
				getProblem4(), getProblem5(), getProblem6(), getProblem7(), getProblem8(), getProblem9(), getProblem10() });

		Collections.shuffle(problems);

		problems.forEach(e -> {
			try {
				document.add(e);
				document.newPage();
			} catch (DocumentException e1) {
				throw new RuntimeException(e1);
			}
		});

		document.close();

		return new ByteArrayInputStream(out.toByteArray());

	}

	static private Chapter getProblem1()
			throws BadElementException, MalformedURLException, IOException, URISyntaxException {
		return getProblem("Problem 1", new String[] { "Problem 1.png" });

	}

	static private Chapter getProblem(String problem, String[] images)
			throws BadElementException, MalformedURLException, IOException {
		Anchor anchor = new Anchor(problem, catFont);
		anchor.setName(problem);

		Chapter catPart = new Chapter(new Paragraph(anchor), 0);

		for (String image : images) {
			Image img = Image.getInstance("images/" + image);

			img.scalePercent(50);

			catPart.add(img);

		}

		return catPart;
	}

	static private Chapter getProblem2() throws BadElementException, MalformedURLException, IOException {
		return getProblem("Problem 2", new String[] { "Problem 2.png" });
	}

	static private Chapter getProblem3() throws BadElementException, MalformedURLException, IOException {
		return getProblem("Problem 3", new String[] { "Problem 3.png" });
	}

	static private Chapter getProblem4() throws BadElementException, MalformedURLException, IOException {
		return getProblem("Problem 4", new String[] { "Problem 4.png" });
	}

	static private Chapter getProblem5() throws BadElementException, MalformedURLException, IOException {
		return getProblem("Problem 5", new String[] { "Problem 5a.png", "Problem 5b.png" });
	}

	static private Chapter getProblem6() throws BadElementException, MalformedURLException, IOException {
		return getProblem("Problem 6", new String[] { "Problem 6a.png", "Problem 6b.png" });
	}

	static private Chapter getProblem7() throws BadElementException, MalformedURLException, IOException {
		return getProblem("Problem 7", new String[] { "Problem 7.png" });
	}

	static private Chapter getProblem8() throws BadElementException, MalformedURLException, IOException {
		return getProblem("Problem 8", new String[] { "Problem 8.png" });
	}
	
	static private Chapter getProblem9() throws BadElementException, MalformedURLException, IOException {
		return getProblem("Problem 9", new String[] { "Problem 9a.png", "Problem 9b.png" });
	}

	static private Chapter getProblem10() throws BadElementException, MalformedURLException, IOException {
		return getProblem("Problem 10", new String[] { "Problem 10.png" });
	}


}
