import Header from '../../layouts/UserDefaultLayout/Header';
import ProductBoxList from '../../components/ProductBoxList';
import { useCart } from '~/api/user/CartContext';
import ProductFeaturedCard from '~/components/ProductFeaturedCard';

export default function HotTrendPage() {
    const { cartItems } = useCart();
    return (
        <>
            <Header cartItems={cartItems} />
            <ProductFeaturedCard />
            <ProductBoxList />
        </>
    );
}
