package com.pracs.electronic.store.helper;

import com.pracs.electronic.store.dtos.PageableResponse;
import com.pracs.electronic.store.dtos.UserDto;
import com.pracs.electronic.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    // U = Entity
    // V = Dto
    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){
        List<U> listOfUserEntity = page.getContent();
        List<V> allUsersDto = listOfUserEntity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());

        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(allUsersDto);
        response.setPageNumber(page.getNumber()+1);
        response.setTotalElements(page.getTotalElements());
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }
}
