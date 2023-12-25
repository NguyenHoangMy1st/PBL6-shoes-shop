import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import React, { useCallback, useEffect } from 'react';
import apiGetAllOrder from '~/api/admin/apiGetAllOrder';

export default function CustomersTable() {
    const fetchData = useCallback(async () => {
        try {
            const response = await apiGetAllOrder.getAllOrder();
            console.log(response);
        } catch (error) {
            console.log(error);
        }
    }, []);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    return (
        <div>
            <TableContainer component={Paper} variant="outlined" className="custom-table-container">
                <Table aria-label="demo table">
                    <TableHead>
                        <TableRow className="custom-header">
                            <TableCell className="custom-header-order">Last Name</TableCell>
                            <TableCell className="custom-header-order">First Name</TableCell>
                            <TableCell className="custom-header-order">Email</TableCell>
                            <TableCell className="custom-header-order">Address</TableCell>
                            <TableCell className="custom-header-order">Mobile</TableCell>
                            <TableCell className="custom-header-order">Delete</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableRow className="custom-cell">
                            <TableCell align="left" className="custom-cell-order"></TableCell>
                            <TableCell align="left" className="custom-cell-order"></TableCell>
                            <TableCell align="left" className="custom-cell-order"></TableCell>
                            <TableCell align="left" className="custom-cell-order"></TableCell>
                            <TableCell align="left" className="custom-cell-order"></TableCell>
                            <TableCell align="left" className="custom-cell-order"></TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
