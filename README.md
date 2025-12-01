# Projeto Biblioteca Java: Sistema de Livraria Digital e E-commerce

**Aplicação Desktop com Java Swing, Banco de Dados MySQL e Arquitetura Modular**

[Logo.png]

---

## 1. Introdução

O **Projeto Biblioteca Java** é uma aplicação desktop desenvolvida em **Java**, utilizando **Java Swing** para a interface gráfica, que simula um sistema completo de livraria virtual e e-commerce. O foco é a **Orientação a Objetos (OO)** e a aplicação de padrões de arquitetura como **DAO (Data Access Object)** e **Singleton**, garantindo um código legível, de fácil manutenção e escalável.

O sistema utiliza o **MySQL** como banco de dados (gerenciado via **XAMPP localhost**) para persistência de dados de usuários, produtos e pedidos.

---

## 2. Funcionalidades Detalhadas

O projeto é dividido em módulos de Cliente e Administração, cada um com um conjunto robusto de funcionalidades.

### 2.1. Módulo do Cliente (Site)

* **Catálogo de Produtos Dinâmico:** Exibe produtos (Livros, CDs e DVDs) com atualização em tempo real via banco de dados. Utiliza herança para diferenciar as classes de produtos (`Produto` como superclasse).
* **Carrinho Inteligente:** Permite operações CRUD em tempo real (adicionar, remover, alterar quantidade) e calcula o valor total automaticamente.
* **Autenticação Avançada:**
    * **Login Sincronizado com o Banco:** Validação de credenciais e controle de sessão.
    * **Recuperação de Senha por Email:** Geração e envio de código de resgate via `EmailSender`.
* **Checkout Completo (`FormularioPedido`):**
    * Fluxo de finalização de compra com coleta de dados de entrega.
    * **Geração de Pagamento:** Simulação de QR Code/Pix para pagamento.
    * **Confirmação por Email:** Envio de email de confirmação do pedido, consolidando todos os detalhes da compra.
* **Gerenciamento de Perfil (`TelaPerfil`):** Permite ao usuário alterar dados cadastrais e fazer o upload de sua foto de perfil.

### 2.2. Módulo de Administração

* **Controle de Acesso:** Login separado (`TelaLoginAdmin`) para acesso restrito.
* **CRUD Completo de Produtos (`CadastrarProduto`):** Operações completas de Cadastro, Leitura, Atualização e Deleção dos itens da loja.
* **Painel de Análise (`TelaAdministracao`):** Área dedicada à análise de dados e performance da loja (potencial para gráficos e relatórios).

---

## 3. Arquitetura e Padrões de Design

O projeto adota uma arquitetura limpa e modular. As classes são separadas por responsabilidade:

| Categoria | Classes Chave | Padrão / Função |
| :--- | :--- | :--- |
| **Persistência** | `UsuarioDAO`, `ConexaoBD` | **DAO** (Acesso a Dados); **Singleton** (`ConexaoBD` para acesso único e seguro). |
| **Sessão** | `SessaoUsuario` | **Singleton** (Gerenciamento de Estado Global do Usuário Logado). |
| **Modelo** | `Produto`, `Livro`, `CD`, `DVD`, `Usuario` | Modelos de Dados, utilizando **Herança** para especialização de produtos. |
| **Interface** | `TelaLogin`, `Menu_Produtos`, `FormularioPedido` | Camada de View e Controller (**MVC Parcial**). |
| **Auxiliar** | `EmailSender` | Classe Estática para serviços de comunicação. |

### 3.1. Design Moderno (Java Swing)

A interface gráfica é um ponto de destaque, utilizando componentes personalizados para garantir uma experiência visual moderna e consistente:

* Classes como `RoundedWindow`, `RoundedPanel`, e `RoundedTextField` são utilizadas para aplicar cantos arredondados e estilos customizados.

---

## 4. Modelo Entidade-Relacionamento (MER)

O esquema de banco de dados (`menuprodutos`) é estruturado para garantir a integridade referencial dos dados.