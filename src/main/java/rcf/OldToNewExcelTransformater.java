package rcf;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OldToNewExcelTransformater {

    private Map<Integer, Type> types = new HashMap<>();

    private Set<Achievment> achievments = new HashSet<>();

    private Cube cube = new Cube();

    public static void main(String[] args) throws Exception {
        new OldToNewExcelTransformater().transform(new File("/home/micha/rcf_spends.xls"), new File("/home/micha/rcf-achievments.xls"));
    }

    private void transform(File data, File achievmentsFile) throws IOException {
        loadAchievments(achievmentsFile);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new ByteArrayInputStream(FileUtils.readFileToByteArray(data)));
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        Iterator<Row> rows = hssfSheet.rowIterator();
        analyseStructure(rows.next());
        while (rows.hasNext()) {
            Row row = rows.next();
            if (row == null) {
                break;
            }
            computeRow(row);
        }
        cube.getUsersByTotal();
        new Achievments(cube, achievments).compute();
        new HighscoreTransformator().transform(cube);
    }

    private void loadAchievments(File file) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new ByteArrayInputStream(FileUtils.readFileToByteArray(file)));
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        Iterator<Row> rows = hssfSheet.rowIterator();
        rows.next();
        while (rows.hasNext()) {
            Row row = rows.next();
            Achievment achievment = new Achievment(
                    row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue(),
                    row.getCell(4).getStringCellValue(),
                    row.getCell(5).getStringCellValue(),
                    row.getCell(6).getStringCellValue(),
                    getAchievmentType(row.getCell(2).getStringCellValue()),
                    getThresholds(row.getCell(3).getStringCellValue().replace("#", ""))
            );
            achievments.add(achievment);
        }
    }

    private List<Integer> getThresholds(String stringCellValue) {
        List<Integer> list = new ArrayList<>();
        if (StringUtils.isNotBlank(stringCellValue)) {
            String[] array = stringCellValue.split(",");
            for (String s : array) {
                list.add(Integer.valueOf(s));
            }
        }
        return list;
    }

    private AchievmentType getAchievmentType(String stringCellValue) {
        return AchievmentType.valueOf(stringCellValue.toUpperCase());
    }

    private void analyseStructure(Row header) {
        int i = 0;
        for (Cell cell : header) {
            Type type = Type.get(cell.getStringCellValue());
            types.put(i, type);
            i ++;
        }
    }

    private void computeRow(Row row) {
        Cell cell = row.getCell(0);
        if (cell != null) {
            String nickname = cell.getStringCellValue();
            register(nickname, row, 1);
            for (int i = 2; i < row.getLastCellNum(); i = i + 3) {
                register(nickname, row, i);
                register(nickname, row, i + 1);
                register(nickname, row, i + 2);
            }
        }
    }

    private void register(String nickanme, Row row, int index) {
        int week = index % 3;
        Cell cell = row.getCell(index);
        Type type = types.get(index);
        if (type == Type.TOTAL) {
            int total = getInt(row.getCell(1));
            cube.onTotal(nickanme, total);
        } else if (type == Type.WIN) {
            cube.onBattle(nickanme, week, getDouble(cell));
        } else if (type == Type.CROWN) {
            cube.onCrown(nickanme, week, getInt(cell));
        } else if (type == Type.DONATION) {
            cube.onDonation(nickanme, week, getInt(cell));
        } else if (type == Type.WEEK) {
            cube.onWeek(nickanme, week, getInt(cell));
        }
    }

    private double getDouble(Cell cell) {
        try {
            return cell.getNumericCellValue();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    private int getInt(Cell cell) {
        try {
            return Double.valueOf(cell.getNumericCellValue()).intValue();
        } catch (NullPointerException e) {
            return 0;
        }
    }

}