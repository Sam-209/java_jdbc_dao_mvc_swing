public class MysqlUserDaoTest {

    private static Connection conn;
    private UserDao dao;

    @BeforeAll
    public static void setupDatabase() throws Exception {
        conn = DriverManager.getConnection(
            "jdbc:mysql://127.0.0.1:3306/mailsystem_test",
            "root",
            "mysql"
        );

        // Limpa tabela
        conn.prepareStatement("DELETE FROM users").execute();
    }

    @BeforeEach
    public void setupDao() {
        dao = new MysqlUserDao(conn);
    }

    @Test
    public void testInsert() throws Exception {
        User u = new User("João", "joao");
        dao.insert(u);

        Assertions.assertNotNull(u.getId());

        User fetched = dao.findById(u.getId());
        Assertions.assertEquals("João", fetched.getName());
        Assertions.assertEquals("joao", fetched.getLogin());
    }

    @Test
    public void testFindByLogin() throws Exception {
        User u = new User("Maria", "maria");
        dao.insert(u);

        User fetched = dao.findByLogin("maria");

        Assertions.assertNotNull(fetched);
        Assertions.assertEquals("Maria", fetched.getName());
    }

    @Test
    public void testDeleteAll() throws Exception {
        dao.insert(new User("A", "a"));
        dao.insert(new User("B", "b"));

        int rows = dao.deleteAll();

        Assertions.assertTrue(rows >= 2);

        Assertions.assertEquals(0, dao.all().size());
    }
}
