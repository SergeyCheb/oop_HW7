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
        //��� �����������:
        // ��� ��_5 ������������� ���� log.txt
        // ��� ��_7 ������������ ���� "fresh_log.txt"
        Logger log = new Logger("fresh_log.txt");
        while (true) {
            String command = prompt("������� �������: ");
            com = Commands.valueOf(command);
            if (com == Commands.EXIT) return;
            switch (com) {
                case CREATE:
                    String firstName = prompt("���: ");
                    String lastName = prompt("�������: ");
                    String phone = prompt("����� ��������: ");
                    User newUser = new User(firstName, lastName, phone);
                    userController.saveUser(newUser);
                    log.addToLogFile("������ ������������ � ID:" + newUser.getId());
                    break;
                case READ:
                    String id = prompt("������������� ������������: ");
                    try {
                        User user = userController.readUser(Long.parseLong(id));
                        log.addToLogFile("������� ������ ������������ � ID:" + user.getId());
                        System.out.println(user);
                        System.out.println();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UPDATE:
                    // ���������� �� ID
                    String idToUpdate = prompt("������������� ������������: ");
                    try {
                        Long tempID = Long.parseLong(idToUpdate);
                        User userToUpdate = userController.readUser(tempID);
                        System.out.println("\t������\n" + userToUpdate + "\n\t����� ��������");
                        userToUpdate.setFirstName(prompt("����� ���: "));
                        userToUpdate.setLastName(prompt("����� �������: "));
                        userToUpdate.setPhone(prompt("����� ����� ��������: "));
                        userController.getRepository().update(userToUpdate);
                        log.addToLogFile("�������� ������������ � ID:" + userToUpdate.getId());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case DELETE:
                    // �������� ������ �� ID
                    String idToDelete = prompt("������������� ������������: ");
                    try {
                        Long idToDel = Long.parseLong(idToDelete);
                        User userToDel = userController.readUser(idToDel);
                        System.out.println("\t������\n" + userToDel + "\n\t����� �������");
                        userController.getRepository().delete(idToDel);
                        log.addToLogFile("������ ������������ � ID:" + idToDel);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case LIST:
                    // ����� ���� �������
                    // ��������� � ��_7
                    try {
                        userController
                                .getRepository()
                                .findAll()
                                .stream()
                                .forEach(user -> System.out.println(user.toStringInLine()));
                        log.addToLogFile("��������� ���� ������ �������������");
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
