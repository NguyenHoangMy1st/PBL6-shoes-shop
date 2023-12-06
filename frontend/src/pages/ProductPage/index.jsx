import Header from "../../components/Layout/Header";
import BrandList from "../../components/BrandList";
import ProductGridList from "../../components/ProductGridList";
import ProductFeaturedCard from "../../components/ProductFeaturedCard";
import { useCart } from "../../contexts/CartContext";

export default function ProductPage() {
  const { cartItems } = useCart();
  return (
    <>
      <Header cartItems={cartItems} />
      <div className="header-search-container container-layout">
        <input
          type="search"
          name="search"
          className="search-field"
          placeholder="Enter your product name..."
        />
        <button className="search-btn">
          <i className="fa fa-search" aria-hidden="true"></i>
        </button>
      </div>
      <BrandList />
      <ProductGridList />
    </>
  );
}
