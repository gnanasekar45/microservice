package com.luminn.firebase.response;

import com.luminn.firebase.dto.UsersDTO;
import com.luminn.firebase.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public class ResponsePage {
    Page<User> pageTut = null;
    List<UsersDTO> userDTO = null;

    public Page<User> getPageTut() {
        return pageTut;
    }

    public void setPageTut(Page<User> pageTut) {
        this.pageTut = pageTut;
    }

    public List<UsersDTO> getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(List<UsersDTO> userDTO) {
        this.userDTO = userDTO;
    }
}
