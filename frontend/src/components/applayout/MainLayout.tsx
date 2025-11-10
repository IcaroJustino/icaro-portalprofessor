import Header from "../header/Header";
import Footer from "../footer/Footer";
import { Outlet } from "react-router-dom";

export default function MainLayout() {
  return (
    <div className="flex flex-col min-h-screen">
      <Header />
      <main className="grow bg-gray-50 p-6">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
}
