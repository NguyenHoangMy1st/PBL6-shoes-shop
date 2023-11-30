import React, { Fragment } from 'react';
import Icon from '../Icons/Icon';

const AdminCustomers = () => {
    return (
        <Fragment>
            <header className={`align-items-center d-flex p-2`}>
                <Icon icon="customers" classes={`ml-2`} />
                <h5 className={`mb-0 ml-4`}>Admin Customers</h5>
            </header>
        </Fragment>
    );
};

export default AdminCustomers;
