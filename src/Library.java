import java.util.ArrayList;
import java.util.List;

/**
 * 图书管理核心逻辑：内存 ArrayList 存放图书，不依赖任何控制台 IO，便于测试。
 *
 * 消息文案统一由本类返回，Main 只负责打印。
 */
public class Library {

    private final List<Book> books = new ArrayList<>();
    private int nextId = 1;

    /**
     * @param withSeedData 是否预置 3 本演示图书（编号 1-3，之后 nextId 从 4 开始）
     */
    public Library(boolean withSeedData) {
        if (withSeedData) {
            add("Java入门", "张三", 59.5);
            add("数据结构", "李四", 45.0);
            add("算法导论", "王五", 88.0);
            // 第二本预置为已借出状态
            findById(2).setBorrowed(true);
        }
    }

    /**
     * 添加图书，分配自增编号（从当前 nextId 开始，只增不减；删除图书不复用编号）。
     *
     * @return 新建的图书对象
     */
    public Book add(String name, String author, double price) {
        Book book = new Book(nextId++, name, author, price);
        books.add(book);
        return book;
    }

    /** 全部图书（按添加顺序，返回副本） */
    public List<Book> listAll() {
        return new ArrayList<>(books);
    }

    /**
     * 按书名关键字搜索，返回书名包含该书名的所有图书。
     *
     * @param keyword 关键字（区分大小写）
     */
    public List<Book> search(String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            // 任务一已修复：按书名 contains 匹配（区分大小写，不匹配作者）。
            if (book.getName().contains(keyword)) {
                result.add(book);
            }
        }
        return result;
    }

    /** 按编号查找图书，不存在返回 null */
    public Book findById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    /** 借阅：未借出 -> 已借出 */
    public String borrow(int id) {
        Book book = findById(id);
        if (book == null) {
            return "图书编号不存在";
        }
        if (book.isBorrowed()) {
            return "该图书已被借出";
        }
        book.setBorrowed(true);
        return "借阅成功";
    }

    /** 归还：已借出 -> 未借出 */
    public String returnBook(int id) {
        Book book = findById(id);
        if (book == null) {
            return "图书编号不存在";
        }
        if (!book.isBorrowed()) {
            return "该图书未被借出";
        }
        book.setBorrowed(false);
        return "归还成功";
    }

    /**
     * 删除图书：按编号查找，存在则从列表中移除并返回"删除成功"，不存在返回"图书编号不存在"。
     * 编号只增不减，删除不回收编号（nextId 不受影响）。
     */
    public String deleteById(int id) {
        Book book = findById(id);
        if (book == null) {
            return "图书编号不存在";
        }
        books.remove(book);
        return "删除成功";
    }
}
