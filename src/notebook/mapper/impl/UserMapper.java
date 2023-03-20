package notebook.mapper.impl;

import notebook.mapper.Mapper;
import notebook.model.User;

public class UserMapper implements Mapper<User, String> {
    @Override
    public String toInput(User user) {
        return String.format("%s:%s_%s;%s", user.getId(), user.getFirstName(), user.getLastName(), user.getPhone());
    }

    @Override
    public User toOutput(String s) {
        String[] lines = s.split("\\p{Punct}");
        long id;
        if (isDigit(lines[0])) {
            id = Long.parseLong(lines[0]);
            return new User(id, lines[1], lines[2], lines[3]);
        }
        throw new NumberFormatException("Id must be a large number");
    }

    private boolean isDigit(String s) throws NumberFormatException {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
