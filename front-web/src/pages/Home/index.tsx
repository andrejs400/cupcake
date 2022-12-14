import React from 'react';
import {ReactComponent as MainImage} from 'core/assets/images/main-image.svg';
import ButtonIcon from 'core/components/ButtonIcon';
import {Link} from 'react-router-dom';
import './styles.scss';

const Home = () =>(
  <div className="home-container">
    <div className="home-content card-base border-radius-20">
      
      <div className="home-text">
        <h1 className="text-title">
          Escolha seu Cupcake <br /> no catálogo de produtos
        </h1>
     
        <p className="text-subtitle">
          Ajudaremos você a encontrar os melhores <br /> produtos doces do mercado.
        </p>
        <Link to="/products" className="startSearchBtn">
          <ButtonIcon text="Inicie agora a sua busca" />
        </Link>
        
      </div>

      <div className="col-6">
        <MainImage className="main-image"/>
      </div>
    </div>
  </div>
);

export default Home;