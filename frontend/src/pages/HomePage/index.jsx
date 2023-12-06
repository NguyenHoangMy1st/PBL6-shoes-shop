import Footer from '../../components/Layout/Footer';
import Header from '../../components/Layout/Header';
import TrendingItem from '../../components/TrendingItem';
import './style.scss';
import ProductFeaturedCard from '../../components/ProductFeaturedCard';
import BrandList from '../../components/BrandList';
import ProductGridList from '../../components/ProductGridList';
import ProductBoxList from '../../components/ProductBoxList';
import { ToastContainer } from 'react-toastify';
import { useCart } from '../../contexts/CartContext';

export default function HomePage() {
    const { cartItems } = useCart();
    return (
        <>
            <Header cartItems={cartItems} />
            <TrendingItem />
            <BrandList />
            <ProductBoxList />
            <ProductFeaturedCard />
            <ToastContainer />
            <ProductGridList />
            <Footer />
        </>
    );
}
