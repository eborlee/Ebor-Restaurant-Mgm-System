package com.ebor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.entity.AddressBook;
import com.ebor.entity.User;
import com.ebor.mapper.AddressBookMapper;
import com.ebor.mapper.UserMapper;
import com.ebor.service.AddressBookService;
import com.ebor.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
