package rcf;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import rcf.achievment.Achievment;
import rcf.achievment.AchievmentType;
import rcf.achievment.Achievments;
import rcf.db.RcfClanWeek;
import rcf.db.RcfUser;
import rcf.service.RcfService;
import system.service.MigrationService;
import system.service.ServiceException;
import system.service.ServiceFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CreateHighscoreFromDb {

    private Cube cube = new Cube();

    public static void main(String[] args) throws ServiceException, IOException {
        MigrationService migrationService = ServiceFactory.getService(MigrationService.class);
        migrationService.migrate(false);
        new CreateHighscoreFromDb().transform(new File("/media/1und1/rcf-achievments.xls"));
    }

    private void transform(File achievmentsFile) throws IOException, ServiceException {
        RcfService rcfService = ServiceFactory.getService(RcfService.class);
        loadAchievments(achievmentsFile);
        List<RcfUser> users = rcfService.getUsers();
        List<RcfClanWeek> weeks = rcfService.getClanWeeks();
        for (RcfUser user : users) {
            int total = 0;
            for (RcfClanWeek week : weeks) {
                int weekInt = getWeek(week);
                int spends = rcfService.getSpendsForWeekAndUser(week, user);
                int crowns = rcfService.getCrownsForWeekAndUser(week, user);
                cube.onDonation(user.getNickname(), weekInt, spends);
                cube.onCrown(user.getNickname(), weekInt, crowns);
                int weekTotal = crowns * 10 + spends;
                total += weekTotal;
                cube.onWeek(user.getNickname(), weekInt, weekTotal);
            }
            cube.onTotal(user.getNickname(), total);
        }
        cube.init();
        Map<String, List<UserAchievment>> userAchievments = new Achievments(cube).compute();
        new HighscoreTransformator().transform(cube, userAchievments);
    }

    private int getWeek(RcfClanWeek week) {
        return week.getYear() * 100 + week.getWeek();
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
                    getThresholds(row.getCell(3).getStringCellValue().replace("#", "")),
                    getInt(row.getCell(7)) == 1,
                    row.getCell(8).getStringCellValue()
            );
            cube.addAchievment(achievment);
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

    private int getInt(Cell cell) {
        try {
            return Double.valueOf(cell.getNumericCellValue()).intValue();
        } catch (NullPointerException e) {
            return 0;
        }
    }

}
