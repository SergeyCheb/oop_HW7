package notebook.view;

import notebook.controller.UserController;
import notebook.logger.Logger;
import notebook.model.User;
import notebook.util.Commands;

import java.util.Scanner;


public class UserView {
    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void run() {
        Commands com;
        //для логирования:
        // для ДЗ_5 использовался файл log.txt
        // для ДЗ_7 используется файл "fresh_log.txt"
        Logger log = new Logger("fresh_log.txt");
        while (true) {
            String command = prompt("Введите команду: ");
            com = Commands.valueOf(command);
            if (com == Commands.EXIT) return;
            switch (com) {
                case CREATE:
                    String firstName = prompt("Имя: ");
                    String lastName = prompt("Фамилия: ");
                    String phone = prompt("Номер телефона: ");
                    User newUser = new User(firstName, lastName, phone);
                    userController.saveUser(newUser);
                    log.addToLogFile("создан пользователь с ID:" + newUser.getId());
                    break;
                case READ:
                    String id = prompt("Идентификатор пользователя: ");
                    try {
                        User user = userController.readUser(Long.parseLong(id));
                        log.addToLogFile("считаны данные пользователя с ID:" + user.getId());
                        System.out.println(user);
                        System.out.println();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UPDATE:
                    // обновление по ID
                    String idToUpdate = prompt("Идентификатор пользователя: ");
                    try {
                        Long tempID = Long.parseLong(idToUpdate);
                        User userToUpdate = userController.readUser(tempID);
                        System.out.println("\tзапись\n" + userToUpdate + "\n\tбудет изменена");
                        userToUpdate.setFirstName(prompt("Новое имя: "));
                        userToUpdate.setLastName(prompt("Новая фамилия: "));
                        userToUpdate.setPhone(prompt("новый номер телефона: "));
                        userController.getRepository().update(userToUpdate);
                        log.addToLogFile("обновлен пользователь с ID:" + userToUpdate.getId());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case DELETE:
                    // удаление записи по ID
                    String idToDelete = prompt("Идентификатор пользователя: ");
                    try {
                        Long idToDel = Long.parseLong(idToDelete);
                        User userToDel = userController.readUser(idToDel);
                        System.out.println("\tзапись\n" + userToDel + "\n\tбудет удалена");
                        userController.getRepository().delete(idToDel);
                        log.addToLogFile("удален пользователь с ID:" + idToDel);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case LIST:
                    // вывод всех записей
                    // добавлено в ДЗ_7
                    try {
                        userController
                                .getRepository()
                                .findAll()
                                .stream()
                                .forEach(user -> System.out.println(user.toStringInLine()));
                        log.addToLogFile("отображен весь список пользователей");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
            }
        }
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }
}
