import java.util.List;
import java.util.Scanner;

/**
 * 控制台入口：只负责菜单显示与输入输出，业务逻辑全部委托 Library。
 */
public class Main {

    private static final String MENU = """
            ===== 图书管理系统 =====
            1. 添加图书
            2. 查看全部图书
            3. 按书名搜索图书
            4. 借阅 / 归还图书
            5. 删除图书
            6. 退出
            请输入编号：""";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library(true);
        boolean running = true;
        while (running) {
            System.out.println(MENU);
            if (!scanner.hasNextLine()) {
                break; // 输入结束（EOF）时安静退出
            }
            String line = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("输入有误，请重新输入");
                continue;
            }
            switch (choice) {
                case 1 -> addFlow(scanner, library);
                case 2 -> listFlow(library);
                case 3 -> searchFlow(scanner, library);
                case 4 -> borrowReturnFlow(scanner, library);
                case 5 -> deleteFlow(scanner, library);
                case 6 -> {
                    System.out.println("感谢使用，再见！");
                    running = false;
                }
                default -> System.out.println("输入有误，请重新输入");
            }
        }
        scanner.close();
    }

    private static void addFlow(Scanner scanner, Library library) {
        System.out.print("请输入书名：");
        String name = scanner.nextLine().trim();
        System.out.print("请输入作者：");
        String author = scanner.nextLine().trim();
        System.out.print("请输入价格：");
        String priceText = scanner.nextLine().trim();
        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            System.out.println("输入有误，请重新输入");
            return;
        }
        Book book = library.add(name, author, price);
        System.out.println("添加成功，编号：" + book.getId());
    }

    private static void listFlow(Library library) {
        List<Book> books = library.listAll();
        if (books.isEmpty()) {
            System.out.println("暂无图书");
            return;
        }
        for (Book book : books) {
            System.out.println(format(book));
        }
    }

    private static void searchFlow(Scanner scanner, Library library) {
        System.out.print("请输入书名关键字：");
        String keyword = scanner.nextLine().trim();
        List<Book> books = library.search(keyword);
        if (books.isEmpty()) {
            System.out.println("未找到相关图书");
            return;
        }
        for (Book book : books) {
            System.out.println(format(book));
        }
    }

    /**
     * 借阅 / 归还（切换语义）：未借出执行借阅，已借出执行归还。
     */
    private static void borrowReturnFlow(Scanner scanner, Library library) {
        System.out.print("请输入图书编号：");
        String idText = scanner.nextLine().trim();
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            System.out.println("输入有误，请重新输入");
            return;
        }
        Book book = library.findById(id);
        if (book == null) {
            System.out.println("图书编号不存在");
            return;
        }
        System.out.println(book.isBorrowed() ? library.returnBook(id) : library.borrow(id));
    }

    /** 删除图书：输入编号，存在则删除并提示结果；非数字提示输入有误，不做二次确认。 */
    private static void deleteFlow(Scanner scanner, Library library) {
        System.out.print("请输入图书编号：");
        String idText = scanner.nextLine().trim();
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            System.out.println("输入有误，请重新输入");
            return;
        }
        System.out.println(library.deleteById(id));
    }

    private static String format(Book book) {
        return String.format("编号:%d  书名:%s  作者:%s  价格:%s  状态:%s",
                book.getId(), book.getName(), book.getAuthor(), book.getPrice(),
                book.isBorrowed() ? "已借出" : "未借出");
    }
}
