package notebook.repository.impl;

import notebook.dao.impl.FileOperation;
import notebook.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.repository.GBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserRepository implements GBRepository<User, Long> {
    private final UserMapper mapper;
    private final FileOperation operation;

    public UserRepository(FileOperation operation) {
        this.mapper = new UserMapper();
        this.operation = operation;
    }

    @Override
    public List<User> findAll() {
        List<String> lines = operation.readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    // не знаю, уместно ли это замечание,
    // но исходя из контекста данной функции,
    // для того чтобы создать пользователя,
    // нужно получить в качестве аргумента
    // пользователя, которого нужно создать -
    // это выглядит как минимум запутанно)
    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id) {
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(mapper.toInput(u));
        }
        operation.saveAll(lines);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    /*
    Для моего случая, как мне кажется, больше подходит тип
    возвращаемого значения boolean. Поэтому тип возвращаемого
    значения изменен на boolean. Так же не вижу смысла передавать ID,
    если в такой сущности как user ID и так есть и его можно получить.
     */
    @Override
    //public Optional<User> update(Long id, User user) {
    public boolean update(User user) {
        String strID = user.getId().toString();
        // получаем все записи из файла в виде строк
        List<String> lst = operation.readAll();
        // ищем строку с нужным id, и если она есть,
        // удаляем ее, а вместо нее подставляем
        // строковое представление нового пользователя
        // ПОДРАЗУМЕВАЕМ, ЧТО ID УНИКАЛЕН И ВСТРЕЧАЕТСЯ МАКСИМУМ 1 РАЗ
        for (int i = 0; i < lst.size(); ++i) {
            if (lst.get(i).startsWith(strID)) {
                lst.remove(i);
                lst.add(i, mapper.toInput(user));
                operation.saveAll(lst);
                return true;
            }
        }
        /*
        в контексте файла UserView.java, эта ветвь недостижима, т.к.
        если пользователь не найден по ID, то исключение будет выброшено
        в строке 47 (User userToUpdate = userController.readUser(tempID);)
        Но логично как мне кажется, описать ситуацию, когда пытаются в
        функцию передать пользователя, которого нет в файле.
         */
        System.out.printf("Пользователя с id %s не найдено%n", strID);
        return false;
    }

    @Override
    public boolean delete(Long id) {
        String strID = id.toString();
        // получаем все записи из файла в виде строк
        List<String> lst = operation.readAll();
        // ищем строку с нужным id, и если она есть,
        // удаляем ее.
        // ПОДРАЗУМЕВАЕМ, ЧТО ID УНИКАЛЕН И ВСТРЕЧАЕТСЯ МАКСИМУМ 1 РАЗ
        for (int i = 0; i < lst.size(); ++i) {
            if (lst.get(i).startsWith(strID)) {
                lst.remove(i);
                operation.saveAll(lst);
                return true;
            }
        }
                /*
        в контексте файла UserView.java, эта ветвь недостижима, т.к.
        если пользователь не найден по ID, то исключение будет выброшено
        в строке 62 (User userToDel = userController.readUser(idToDel);)
        Но логично как мне кажется, описать ситуацию, когда пытаются в
        функцию передать ID, которого нет в файле.
         */
        System.out.printf("Пользователя с id %s не найдено%n", strID);
        return false;
    }
}
