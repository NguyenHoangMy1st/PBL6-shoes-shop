import Header from '../../layouts/UserDefaultLayout/Header';
import ProductBoxList from '../../components/ProductBoxList';
import { useCart } from '~/api/user/CartContext';
<<<<<<< HEAD
import ProductFeaturedCard from '~/components/ProductFeaturedCard';
=======
import Footer from '~/layouts/UserDefaultLayout/Footer';
import ProductFeaturedList from '~/components/ProductFeaturedList';
>>>>>>> 8fa5cad29f257abb86a5b24b39e68f7e7a127e9b

export default function HotTrendPage() {
    const { cartItems } = useCart();
    return (
        <>
            <Header cartItems={cartItems} />
            <ProductFeaturedList />
            <ProductBoxList />
            <Footer />
        </>
    );
}
