package com.pyro.service;

import com.pyro.entities.Category;
import com.pyro.entities.Product;
import com.pyro.repositories.CatRepository;
import com.pyro.repositories.ProductRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Toxls {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CatRepository catRepository;

    @Transactional
    @SuppressWarnings("deprecation")
    public void writeIntoExcel(String file) throws FileNotFoundException, IOException {


        Workbook book = new HSSFWorkbook();
        CellStyle style = book.createCellStyle();
        style.setBorderTop((short) 6); // double lines border
        style.setBorderBottom((short) 1); // single line border
        Font font =  book.createFont();
        font.setFontHeightInPoints((short) 15);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        Sheet sheet = book.createSheet("Goods");
        // Нумерация начинается с нуля
        Row row;
        Cell cell1, cell2;
        List<Category> categories = catRepository.findAll();
        //List<Product> products = productRepository.findAll();
        for (int i = 0, r=0; i<categories.size(); i++) {
            row = sheet.createRow(r);
            cell1 = row.createCell(0);
            cell1.setCellValue(categories.get(i).getName());
            cell1.setCellStyle(style);
            r++;
            List<Product> products =productRepository.findByCategory(categories.get(i));
            for (int j = 0; j< products.size(); j++) {

                row = sheet.createRow(r);
                cell1 = row.createCell(0);
                cell2 = row.createCell(1);
                cell1.setCellValue(products.get(j).getName());
                cell2.setCellValue(products.get(j).getCost());
                r++;
            }
        }

        // Меняем размер столбца
        //sheet.autoSizeColumn(1);

        // Записываем всё в файл
        book.write(new FileOutputStream(file));
        book.close();
    }

    @Transactional
    public void writeIntoWord(String file) throws FileNotFoundException, IOException {
        //Blank Document
        XWPFDocument document = new XWPFDocument();
        //Create a blank spreadsheet
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("At tutorialspoint.com, we strive hard to " +
                        "provide quality tutorials for self-learning " +
                        "purpose in the domains of Academics, Information " +
                        "Technology, Management and Computer Programming Languages.");
        //Write the Document in file system
        FileOutputStream out = new FileOutputStream( new File(file));
        document.write(out);
        out.close();
        System.out.println("createdocument.docx written successully");
    }
}
