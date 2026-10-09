import java.util.List;

/**
 * 考核新增测试（任务三）。
 *
 * 覆盖：
 *  - 任务一 search：按书名 contains 匹配、区分大小写、不匹配作者。
 *    用例关键字：Java / 入门 / 张 / java / 数据结构。
 *  - 任务二 删除图书：删除成功、listAll 不含被删图书、删 2 后 add 得 4、
 *    重复删除不存在、删空后编号延续。
 *
 * 约定：类名以 Test 结尾；测试方法为无参 public void 且以 test 开头；使用 Assert。
 */
public class ExamTest {

    /** 种子数据：1=Java入门/张三，2=数据结构/李四（已借出），3=算法导论/王五。 */
    private Library seeded() {
        return new Library(true);
    }

    /** T1：search("Java") 命中《Java入门》。 */
    public void testSearchJava() {
        Library library = seeded();
        List<Book> hits = library.search("Java");
        Assert.assertEquals(1, hits.size(), "search(\"Java\") 应命中 1 本");
        Assert.assertEquals(1, hits.get(0).getId(), "命中编号应为 1");
        Assert.assertEquals("Java入门", hits.get(0).getName(), "命中书名应为 Java入门");
    }

    /** T1：search("入门") 按书名包含命中《Java入门》。 */
    public void testSearchRuMen() {
        Library library = seeded();
        List<Book> hits = library.search("入门");
        Assert.assertEquals(1, hits.size(), "search(\"入门\") 应命中 1 本");
        Assert.assertEquals("Java入门", hits.get(0).getName(), "命中书名应为 Java入门");
    }

    /** T1：search("张") 不应命中作者为「张三」的书（只匹配书名）。 */
    public void testSearchZhangNotMatchAuthor() {
        Library library = seeded();
        Assert.assertEquals(0, library.search("张").size(), "作者名不应被匹配");
    }

    /** T1：search("java") 小写不应命中《Java入门》（区分大小写）。 */
    public void testSearchLowerCaseJava() {
        Library library = seeded();
        Assert.assertEquals(0, library.search("java").size(), "小写 java 不应命中");
    }

    /** T1：search("数据结构") 命中《数据结构》。 */
    public void testSearchShuJuJieGou() {
        Library library = seeded();
        List<Book> hits = library.search("数据结构");
        Assert.assertEquals(1, hits.size(), "search(\"数据结构\") 应命中 1 本");
        Assert.assertEquals(2, hits.get(0).getId(), "命中编号应为 2");
    }

    /** T2：删除存在编号返回「删除成功」，且 listAll 不再包含该图书。 */
    public void testDeleteSuccessThenListAllExcludes() {
        Library library = seeded();
        Assert.assertEquals("删除成功", library.deleteById(2), "删除存在图书应返回删除成功");
        List<Book> books = library.listAll();
        Assert.assertEquals(2, books.size(), "删除后应剩 2 本");
        for (Book b : books) {
            Assert.assertFalse(b.getId() == 2, "listAll 不应再包含编号 2");
        }
        Assert.assertNull(library.findById(2), "findById(2) 删除后应为 null");
        Assert.assertEquals(0, library.search("数据结构").size(), "search 不应再命中被删图书");
    }

    /** T2：删除编号 2 后新增，编号应延续为 4（不复用已删编号）。 */
    public void testDeleteThenAddGetsNextId() {
        Library library = seeded();
        Assert.assertEquals("删除成功", library.deleteById(2), "删除编号 2 应成功");
        Book added = library.add("新书", "作者", 1.0);
        Assert.assertEquals(4, added.getId(), "删 2 后新增应得编号 4");
    }

    /** T2：重复删除 / 删除不存在编号，均返回「图书编号不存在」。 */
    public void testDeleteNonExistentTwice() {
        Library library = seeded();
        Assert.assertEquals("删除成功", library.deleteById(3), "首次删除编号 3 应成功");
        Assert.assertEquals("图书编号不存在", library.deleteById(3), "重复删除应提示编号不存在");
        Assert.assertEquals("图书编号不存在", library.deleteById(999), "删除不存在编号应提示编号不存在");
    }

    /** T2：删空后编号继续延续（不复用）。 */
    public void testIdsContinueAfterDeletingAll() {
        Library library = new Library(false);
        Assert.assertEquals(1, library.add("A", "a", 1.0).getId(), "第 1 本编号应为 1");
        Assert.assertEquals(2, library.add("B", "b", 2.0).getId(), "第 2 本编号应为 2");
        Assert.assertEquals(3, library.add("C", "c", 3.0).getId(), "第 3 本编号应为 3");
        Assert.assertEquals("删除成功", library.deleteById(1), "删除编号 1");
        Assert.assertEquals("删除成功", library.deleteById(2), "删除编号 2");
        Assert.assertEquals("删除成功", library.deleteById(3), "删除编号 3");
        Assert.assertEquals(0, library.listAll().size(), "删空后应为 0 本");
        Assert.assertEquals(4, library.add("D", "d", 4.0).getId(), "删空后新增应得编号 4");
    }

    /** T2：删除已借出的图书后，借还全链路一致（按编号提示不存在）。 */
    public void testDeleteThenBorrowReturnConsistent() {
        Library library = seeded();
        Assert.assertEquals("删除成功", library.deleteById(2), "删除编号 2 应成功");
        Assert.assertEquals("图书编号不存在", library.borrow(2), "删除后借阅应提示编号不存在");
        Assert.assertEquals("图书编号不存在", library.returnBook(2), "删除后归还应提示编号不存在");
        Assert.assertEquals(2, library.listAll().size(), "删除后应剩 2 本");
    }
}