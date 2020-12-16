package com.digitzones.procurement.dao.impl;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.ICustomerDao;
import com.digitzones.procurement.model.Customer;
import org.springframework.stereotype.Repository;
@Repository
public class CustomerDaoImpl extends CommonDaoImpl<Customer> implements ICustomerDao {
    public CustomerDaoImpl() {
        super(Customer.class);
    }
}
