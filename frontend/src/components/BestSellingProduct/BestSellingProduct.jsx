import React, { useEffect } from 'react';
import classNames from 'classnames/bind';
import styles from './BestSellingProduct.module.scss';

const cx = classNames.bind(styles);

/**
 {
    "productId": 3,
    "title": "Adidas Originals",
    "imageUrl": "https://dl.dropboxusercontent.com/scl/fi/s4gtldvcycux8ew8ai7oi/shoes2.png?rlkey=7qx67asf6o6pjn12ez7h8kq5b&dl=0",
    "discountedPrice": 250000,
    "appearanceCount": 8
}
 */

const BestSellingProduct = ({ selectedTime, setIsLoading }) => {
    useEffect(() => {
        console.log('selected time for best selling product: ', selectedTime);
        // call api
    }, [selectedTime]);
    return (
        <div className={`align-items-center container d-flex h-100 justify-content-center w-100`}>
            <div className={`${cx('image-box')} align-items-center d-flex flex-column h-75 justify-content-center`}>
                <img
                    className={cx('image')}
                    src="https://dl.dropboxusercontent.com/scl/fi/s4gtldvcycux8ew8ai7oi/shoes2.png?rlkey=7qx67asf6o6pjn12ez7h8kq5b&dl=0"
                    alt=""
                />

                <div className={cx('product-name')}>Adidas Originals</div>
            </div>
            <div
                className={`${cx(
                    'des-box',
                )} align-items-center d-flex flex-column h-100 image-box justify-content-center`}
            >
                <div className={cx('des-content')}>
                    <h6 className={cx('des-content-title')}>Discounted Price</h6>
                    <h5 className={cx('des-content-value')}>250000</h5>
                </div>
                <div className={cx('des-content')}>
                    <h6 className={cx('des-content-title')}>Quantity Sold</h6>
                    <h5 className={cx('des-content-value')}>8</h5>
                </div>
            </div>
        </div>
    );
};

export default BestSellingProduct;
