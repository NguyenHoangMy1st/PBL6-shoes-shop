import React from 'react';
import { Link } from 'react-router-dom';
import './style-prefix.scss';
<<<<<<< HEAD

export default function ProductBoxCard({ id, image, title, brandId, brandName, price, discountedPrice }) {
=======
export default function ProductBoxCard({ id, image, title, brand, price, discountedPrice }) {
>>>>>>> 8fa5cad29f257abb86a5b24b39e68f7e7a127e9b
    return (
        <div>
            <div className="product-minimal">
                <div className="product-minimal-content">
                    <Link to={`/product/${id}`} className="product-minimal-link">
                        <img src={image} alt="" className="product-minimal-image"></img>
                    </Link>
                    <div className="product-minimal-detail">
                        <h2 className="product-minimal-name">
                            <Link to={`/product/${id}`} className="product-minimal-name-link">
                                {title}
                            </Link>
                        </h2>
                        <Link to={`/product?brand=${brandId}`} className="product-minimal-category">
                            {brandName}
                        </Link>
                        <div className="product-minimal-price">
                            <p className="product-minimal-price-real">
                                {price?.toLocaleString('it-IT', {
                                    style: 'currency',
                                    currency: 'VND',
                                })}
                            </p>
                            <del>
                                {discountedPrice?.toLocaleString('it-IT', {
                                    style: 'currency',
                                    currency: 'VND',
                                })}
                            </del>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
