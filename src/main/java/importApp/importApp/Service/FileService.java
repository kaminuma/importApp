package importApp.importApp.Service;

import importApp.importApp.Entity.taskEntity;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class FileService {

    // ファイルのフォーマットを確認
    public boolean isValidFormat(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            // フォーマットが正しいかの確認ロジックを追加
            // 例: シート名やセルの内容の確認
            // ...

            return true; // フォーマットが正しい場合
        } catch (Exception e) {
            return false; // フォーマットが正しくない場合
        }
    }

    // ファイルデータの処理
    public List<taskEntity> parseExcelFile(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 1列目のシートを選択

            List<taskEntity> tasks = new ArrayList<>();

            Iterator<Row> iterator = sheet.iterator();

// ヘッダ行をスキップする
            if (iterator.hasNext()) {
                iterator.next();
            }

            while (iterator.hasNext()) {
                Row row = iterator.next();

                // A列のセルを取得
                Cell taskNameCell = row.getCell(0); // A列のセルを取得

                if (taskNameCell == null || taskNameCell.getCellType() != CellType.STRING || taskNameCell.getStringCellValue().isEmpty()) {
                    // A列が空白の場合、ループを終了
                    break;
                }

                Cell descriptionCell = row.getCell(1);
                Cell dueDateCell = row.getCell(2);
                Cell priorityCell = row.getCell(3);
                Cell statusCell = row.getCell(4);
                Cell projectIdCell = row.getCell(5);
                Cell userIdCell = row.getCell(6);

                // セルからデータを取得してTaskオブジェクトを作成
                String taskName = taskNameCell.getStringCellValue();
                String description = descriptionCell.getStringCellValue();
                Date dueDate = dueDateCell.getDateCellValue();
                String priority = priorityCell.getStringCellValue();
                String status = statusCell.getStringCellValue();
                long projectId = (long) projectIdCell.getNumericCellValue();
                long userId = (long) userIdCell.getNumericCellValue();

                taskEntity task = new taskEntity(taskName, description, dueDate, priority, status, projectId, userId);
                tasks.add(task);
            }

            // tasksリストをデータベースに保存するか、必要に応じて処理を行う

            return tasks;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
