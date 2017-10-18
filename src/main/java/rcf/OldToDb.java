package rcf;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import rcf.db.RcfUser;
import rcf.extract.ExtractedUser;
import rcf.extract.Extractor;
import rcf.service.RcfService;
import system.service.MigrationService;
import system.service.ServiceException;
import system.service.ServiceFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Michael Lieshoff
 *
 * Job to transform from old excel file, runs only one time.
 */
public class OldToDb {

    private Map<Integer, Type> types = new HashMap<>();
    private Map<Integer, String> headerName = new HashMap<>();

    private final RcfService rcfService = ServiceFactory.getService(RcfService.class);

    public static void main(String[] args) throws Exception {
        MigrationService migrationService = ServiceFactory.getService(MigrationService.class);
        migrationService.migrate(false);
        new OldToDb().transform(new File("/media/micha/1und1/rcf_spends.xls"));
    }

    private void transform(File data) throws IOException, ServiceException {
        Map<String, ExtractedUser> extractedUsers = new Extractor().start();
        rcfService.updateUsers(extractedUsers);
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
    }

    private void analyseStructure(Row header) {
        int i = 0;
        for (Cell cell : header) {
            Type type = Type.get(cell.getStringCellValue());
            types.put(i, type);
            headerName.put(i, cell.getStringCellValue());
            i ++;
        }
    }

    private void computeRow(Row row) throws ServiceException {
        Cell cell = row.getCell(0);
        if (cell != null) {
            String nickname = cell.getStringCellValue();
            if (rcfService.exists(nickname)) {
                RcfUser rcfUser = rcfService.getUserByNick(nickname);
                register(rcfUser, row, 1);
                for (int i = 2; i < row.getLastCellNum(); i = i + 3) {
                    register(rcfUser, row, i);
                    register(rcfUser, row, i + 1);
                    register(rcfUser, row, i + 2);
                }
            }
        }
    }

    private void register(RcfUser rcfUser, Row row, int index) throws ServiceException {
        Cell cell = row.getCell(index);
        Type type = types.get(index);
        if (type == Type.WIN) {
            int crowns = (int) (getDouble(cell) * 60);
            int week = getWeek(index);
            rcfService.setCrowns(2017, week, rcfUser, crowns);
        } else if (type == Type.CROWN) {
            int crowns = getInt(cell);
            int week = getWeek(index);
            rcfService.setCrowns(2017, week, rcfUser, crowns);
        } else if (type == Type.DONATION) {
            int spends = getInt(cell);
            int week = getWeek(index);
            rcfService.setSpends(2017, week, rcfUser, spends);
        }
    }

    private int getWeek(int index) {
        String header = headerName.get(index);
        return Integer.valueOf(header.substring(header.length() - 2));
    }

    private double getDouble(Cell cell) {
        try {
            return cell.getNumericCellValue();
        } catch (IllegalStateException e) {
            String s = cell.getStringCellValue();
            return Double.valueOf(s);
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
