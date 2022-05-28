# Spring boot

```xml
<parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>2.2.4.RELEASE</version>
</parent>
```

xml base para inicilização de um projeto spring,

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter</artifactId>
</dependency>

```

dependencia base para inicialização e build de um projeto spring

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

```

dependencia para rodarmos o projeto spring como api rest, podendo usar anotações como **@RestController**
e **@GetMapping**

**@SpringBootApplication** = anotação colocada na main do projeto para dizer que é a classe de inicilização do spring

```java
SpringApplication.run(ClasseMain.class, args);
```

**@RestController** = serve para dizer ao spring que aquela classe vai ter métodos http e responder a chamadas de api, como se fosse um controller mesmo de rotas

**@GetMapping** = anotação que diz ao spring para usar aquela função para responder a uma chamada GET na URL dada, por exemplo:

```java
@GetMapping("/teste")
public String nomeQualquer(){
	return "opa"
}
```

**@Configuration** = anotação que diz ao spring que aquela é uma classe de configuração, podem ser diversos tipos de configs (**@Bean**) como nomes de campos, cores, conexões com o banco (tp o config.json)

**@Bean** = cria uma config dentro da @Configuration e da um nome a ela para ser acessa mais facilment
exemplo

```java

@Bean(name = "applicationName")
public String nomeQualquer() {
	return "Minha Aplicação";
}
```

**@Qualifier** = chama e coloca o valor de um @Bean em uma variável
exemplo:

```java

@Qualifier("applicationName")
private String nome2;
```

**Container IOC** = Container de inversão de controller
delegamos a ele tarefas como crição de configurações e criação de conexões com bd por exemplo.

Ele armazena essas configs e todos os objetos configurados

**@Components** = classes que podem ser uteis para a aplicação.
podem ser **@Controllers**, **@Repository** ou **@Services**

**@Controllers** = controladores, camada que faz a intermediação entre a view e model

**@Repository** = faz parte da camada de modelo/dados acessa repositorios de dados como banco de dados

**@Service** = camada de serviço onde operam os objetos/classes de servico onde tem regras de negócios e processamentos que devem ser feitos pela aplicação (como validacoes etc)

essas 3 herdam de component, caso a sua classe não seja desses tipos podemos anotar diretamente com **@Component** que ela vai ser gerenciada diretamente pelo spring (vai ficar bem genérica)

Com essas anotações **@Repository**, o spring vai criar uma instância e apenas uma daquele objeto para a aplicação inteira, independente do número de usuários, essa instância será criada com o contexto "singleton". Essa instância vai ser adicioanda no container de injeção e pronta para injetar aonde eu precisar, sem ficar instânciando ela toda hora;

**@ComponentScan** = anotação responsável para dizer quais componentes/classes de configs/objetos que devemos escanear atrás de uma das anotação citadas acima

exemplo: 

```java
@ComponentScan(basePackages = {"pacotes1", "pacotes2"})
```

## Injeção de dependência

delego a outras classes ou um container (provedor) que instancie minhas classes/dependencias e injete aonde eu preciso.

### Como injetar dependências

Cria-se um construtor com a anotação **@Autowired** na classe que quer injetar e passo como parâmero a minha dependência
exemplo: 

```java
@Autowired
public ClientesService(ClientesRepository repository){
	this.repository = repository;
}
```

Também podemos injetar a dependência diretamente na declaração da propriedade na classe, sem a necessidade do constructor, apenas com a anotação @Autowired emcima da propriedade.
por exemplo:

```java
@Autowired
private ClientesRepository repository;
```

Também é possível setar através de métodos setter, por exemplo

```java
@Autowired
public void setRepository(ClientesRepository repository){
	this.repository = repository;
}
```

## **Application.properties** e definições de ambiente

**application.properties** = Arquivo que trabalha no esquema chave e valor, podemos fazer configs customizadas sem precisar usar o **@Bean** 

Existem muitas configs que o próprio spring disponibiliza que podem ser vistas neste [link](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html) 

Exemplo:

```xml
application.name=Opa
```

### Usando valores do application.properties

**@Value("${chave}")** = o spring vai scannear o arquivo properties e através da notação **@Value**, injetar o valor na variável referida
exemplo:

```java
@Value("${[application.name](http://application.name/)}")
private String applicationName;
```

properties de exemplo:

```xml
//porta do server
server.port = 8081 
//uma url base para acessar o servidor, como o api/v2
server.servlet.context-path=/sistemaVendas 
```

**Configurações através de perfis**

 diferentes application.properties para diferentes ambientes
o arquivo application.properties é um arquivo de convenção, é o padrão. Para criarmos outros arquivos devemos criar na pasta resources (mesma pasta do **application.properties**) arquivos com nomes como "**application-ambiente.properties**", onde "**ambiente**" pode ser qualquer nome desejado.

### Como subir o spring com o application especifico

no "**application.properties**" principal, devemos usar a seguinte chave

```xml
spring.profiles.active=production
```

onde o valor production é o nome do ambiente definido no nome do arquivo **application-ambiente.properties**, trata-se desse "**ambiente**".

exemplo:
um profile chamado **application-meuTeste.properties**
para ser usado, devemos no **application.properties** principal usar a seguinte linha
**spring.profiles.active=meuTeste**

Também é possível definir que uma **@Configuration** só fique disposnível em determinado ambiente com a anotation **@Profile("ambiente")** onde ambiente é o mesmo nome do ambiente anteriormente definido
exemplo:

```java
@Configuration
@Profile("production")
public class MinhaConfig{}
```

## **CommandLineRunner**

são funções que juntamente com a anotação **@Bean**, são executadas assim que o spring inicializa, o spring procura essas funções com **@Bean** e executa elas imediatamente.
exemplo:

```java
@Bean
public CommandLineRunner executar(){
	return args -> {
		System.out.println("Rodando a config de production");
	};
}
```

**@Autowired** = uso para injeção de dependências, como quando usamos o **@Qualifier("nomeDeUmBean")** para injetar dados em uma variavel é necessário usar o **@Autowired** para dizer que estamos injetando dados

## Criando anotações personalizadas

devemos criar uma classe do tipo "**@interface**" para definir ela como uma anotation, com as anotações **@Retention(RetentionPolicy.RUNTIME)** e **@Target(ElementType.TYPE)**, esse "**ElementType.Type**" serve para dizer que só poderemos usar essa anotação criada por nós em classes do java.
exemplo:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//dizemos que é uma anotação de configuração, pode ser de qualquer tipo como 
//@Repository ou @Controller
@Configuration
//nome do tipo de ambiente que queremos que essa anotation rode, 
//ou seja ela só roda quando a aplicação estiver usando esse ambiente
@Profile("development")
public @interface Development {}
```

Dessa forma, podemos definir nossas configs usando apenas uma anotation.

O mesmo pode ser feito para **@Bean** criados em **@Configuration**, basta seguir o exemplo abaixo

```java

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Autowired
//O @Qualifier serve para injetar um @Bean chamado "cao"
@Qualifier("cao")
public @interface Cao {}
```

onde existe uma **@Bean** chamada "cao" em uma **@Configuration**, agora temos uma anotação chamada **@cao** que simplifica esse uso de injeção de dados.
Com a diferença que o **@Target** agora possui o valor “**ElementType.FIELD”** ao invés de “**ElementType.TYPE”** para podermos injetar em propriedades ao invés de classes.

## Dependência para se trabalhar com dados, persistência e bancos de daos no spring

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

prefixo de configurações de bancos de dados no spring: "**spring.datasource**", ou seja tudo o que começa com isso no **application.properties** é config de banco

configs de conexão para o h2 database no **application.properties**:

```xml
spring.datasource.url=jdbc:h2:mem:testdb
//toda a conexão com banco em java começa com jdbc
//o 'mem' ali da conexão significa que o banco é em memória
spring.datasource.driverClassName=org.h2.Driver
//também pode ser usado driver-class-name
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## Data.sql e JDBC

Na pasta **resources**, criamos um arquivo "**data.sql**" que vai rodar sqls toda vez que a aplicação for inicializada, em um banco em memória por exemplo isso é bom pois as tables são sempre destruidas quando a aplicação é parada, assim sempre que rodarmos ela teremos certeza que a aplicação vai ter as tabelas corretas no banco.

Quando anotamos uma classe com **@Repository** elá é um **@Component** como qualquer outro, mas também o spring vai entender que essa classe faz operações na base de dados e todas as exceptions que ocorrem na base de dados vão ser traduzidas e mostradas corretamente pela classe. Isso se chama exception translation e só ocorre no **@Repository**

O "**Spring.data.jpa**" provem uma classe de acesso ao banco de dados chamada de "**JdbcTemplate**", essa classe tem diversos métodos que permitem que façamos operaçãoes na base de dados.
Exemplo:

```java
@Autowired
private JdbcTemplate jdbcTemplate;
```

"**jdbcTemplate.update()**" executa scripts sql de **atualização insersão e delete**, recebe os seguintes parâmetros "**(String sql, new Object[]{})**"
**//onde new Objec é um array de objetos contendo os valores da query sql**
exemplo:

```java
private static final String INSERT = "inser into client (nome) values (?)";
jdbcTemplate.update(INSERT, new Object[]{client.getName()});
```

### Fazendo querys o jdbc:

a função "**jdbcTemplate.query()**" recebe 2 parâmetros, o primeiro sendo a query e o segundo sendo um **RowMapper<TipoDaBusca>**, esse **RowMapper** serve para mapear/parcorrer os valores obtidos na query e retornar uma lista deles, podemos manipular esses dados antes de criar um novo objeto reecrevendo o método mapRow do RowMapper como no exemplo a seguir:

```java
public List<Client> getAll(){
	return jdbcTemplate.query(SELECT_ALL, new RowMapper<Client>() {
		@Override
		public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Client(rs.getString("name"));
		}
	});
}
```

Método de reference do java 8 "**System.out::println**" pesquisar mais sobre, é necessário ter um método oString no objetos utilizado com esse tipo de println
Exemplo:

```java
{1,2,3}.forEach(System.out::println)
```

## JPA

JPA é uma especificação que não precisa escrever SQL puro como no jdbc, ele é um ORM (object realtiona mapping).
**@Entity**: Para mapear uma entidade dizendo que ela é uma JPA, precisamos colocar a anotação **@Entity** e **@Table** do pacote **javax.persistence**, a **@Table** não é obrigatoria a menos que o nome da sua entidade seja direferete da sua table na classe
exemplo:

```java
@Entity
@Table
public class Clients {}
```

exemplo de uso obrigatório da anotação **@Table**

```java
@Entity
@Table(name = "Cliente", schemas = "Venda") 
//isso aqui vai criar a table com o nome "Cliente"
//schema é para ser utilizado em bancos que trabalham com esquemas
public class Clients_naoEhMeuNomeNoBanco {}
```

Com o **@Entity** estou dizendo para o JPA que essa classe deve ser escaneada e criada no banco de dados como uma table

**@Id**: Anotação para definir qual a primary key da entidade, uma classe com o **@Entity** deve ter ao menos a anotation **@Id**
exemplo:

```java
@Entity
@Table
public class Clients_naoEhMeuNomeNoBanco {
	@Id
	auto_increment do sql
	private Integer id;
}
```

**@GeneratedValue**: Anotação utilziada para dizer que o valor vai ser auto_increment, normalmente usada junto com a anotação **@Id**, ela possui o campo strategy dentro dela que serve para definir como será feita a geração desses valores, no exemplo abaixo usamos a estratégia AUTO para gerar de 1 em 1 automaticamente
exemplo:

```java
public class Clients_naoEhMeuNomeNoBanco {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) //esta anotação funciona como auto_increment do sql
	private Integer id;
}
```

**@Column**: Não é obrigatório se o nome da propriedade for igual o da propriedade do banco, é o mesmo caso do **@Table**. Outras definições podem ser feitas como se é unico ou se pode ser nulo, consultar documentação do JPA dessa anotacao.
Exemplo:

```java
public class Clients_naoEhMeuNomeNoBanco {
	@Id
	@Column(name = "id")
	private Integer idMasNaoEhOMeuNome;
}
```

Para demais propriedades do objeto(além do @Id) não é necessária nenhuma anotação, pois ao usar o **@Entity** ele já reconhece automaticamente que se tratam de colunas
exemplo:

```java
public class Clients_naoEhMeuNomeNoBanco {
	@Id
	@Column(name = "id")
	private Integer idMasNaoEhOMeuNome;
	private String name;
	private Integer age;
	//essas duas propriedades vão existir nessa table no banco automaticamente
	//com esse mesmo nome ATENÇÃO NISSO
}
```

Nesse caso de usar o **@Entity** não é mais necessário usar o arquivo "**data.sql**", pois ele já vai utilizar o **@Entity** para mapear e criar as tables no banco

Para trabalhar com essa table no **@Repository** usamos algo parecido com o JDBCTemplate, mas é o EntityManager que gerencia essas entidades criadas com o **@Entity**, ela que faz todas as operações na base sem usar SQL
exemplo:

```java

@Autowired 
//@Autowired para o spring injetar mesmo na hora de buildar
private EntityManager entityManager;
```

**@Transactional:** Neste caso no **@Repository** precisamos anotas os métodos de manipulação com **@Transactional** do **pacote org.springframework.transaction.annotation.Transactional**.
exemplo:

```java
@Transactional
public Client save(Client client) {
	entityManager.persist(client);
	return client;
}
```

**entityManager.persist()**: usado para persistir dados na base diretamente, igual um create do mongo, até salvarmos ela é uma entidade transiente, ela não está no banco nem tem ID, após salvar ela passa a ser gerenciada pelo **EntityManager** e fica no **cache**. Quando tentar obter essa entidade dependendo do método não é preciso nem ir na base de dados
**entityManager.merge()**: atualizar do uma entidade JPA, o update do mongo. As entidade JPA tem um estado. O manager verifica se a nova entidade passada está diferente da entidade salva no banco/manager, se estiver ele atualiza. 

**entityManager.find()**= quando queremos buscar um elemento pela chave primária devemos passar a classe e o id desse elemento
exemplo:

```java
entityManager.find(Client.class, cliend.getId());
```

**@Transactional(readOnly = true)** para apenas leitura, fica mais otimizado não deixando nada no cache

Busca por outras propridades (nome por exemplo):
Usamos uma consulta jpql, tipo sql mas referenciando as entidades ao invés das tables
exemplo:

```java
@Transactional(readOnly = true)
public List<Client> getByName(String name) {
	String jpql = "select c from Client c where [c.name](http://c.name/) like :name";
	TypedQuery<Client> query = entityManager.createQuery(jpql, Client.class);
	List<Client> clientList = query.setParameter("name", "%" + name + "%").getResultList();
	return clientList;
}
```

onde "**select c from Client c where [c.name](http://c.name/) like :name**"; o "**c**" é a entidade, como * do sql podemos usar “[**c.id](http://c.id/), [c.name](http://c.name/)**” etc. O **:name** será o nome do param que usaremos na query, deve ter os **:** colados nele, tipo "**:name**"
**query.setParameter(name que será o nome do parametro no nosso caso o "name", value a ser colocado no ":name");**

Buscar todos os elementos por exemplo:

```java
public List<Client> getAll(){
return entityManager.createQuery("from Client", Client.class).getResultList();
}
```

neste caso pode ser direto, pois não estamos passando nenhum parâmetro para limitar a busca

## JPA Repository

O spring data criou algumas interfaces que permitem já termos acesso a métodos de busca, delete e update de entidade.
Para isso alterarmos a classe que usa a anotação **@Repository** para uma interface que extende **JPARepository** que recebe 2 parâmetros que são a entidade e o tipo o identificador:
exemplo:

```java
public interface Clients extends JpaRepository<Client, Integer> {}
```

Os JPARepositories já tem um entity manager dentro deles.
Método **save(entidade)**: método do JpaRepository para salvar e atualizar entidade
Método **findAll()**: método do JpaRepository para buscar todas as entidade
Método **delete(entidade)**: método do JpaRepository para deleter uma entidade
A interface **JpaRepository** possui vários métodos já implementados

### Query methods:

É um método que vira uma query. Você declara uma certa convenção na sua inteface que extende JpaRepository e o spring transforma ela numa query
exemplo da conveção:

```java
List<Client> findByNameLike(String name);
```

onde
**find** = dizemos que a query será do tipo busca (poderia ser save, delete etc)
**ByName** = a query será feita com base na propriedade passada, nesse caso Name
**Like** = que a query deve rodar com o parâmetro "Like" como no sql
**String name** = o parâmetro para a query

Podemos utilizar padrões de HQL nesses métodos
exemplo:

```java
List<Client> findByNameOrId(String name, Integer id);
```

onde
**find** = dizemos que a query será do tipo busca (poderia ser save, delete etc)
**ByName** = a query será feita com base na propriedade passada, nesse caso Name
**OrId** = parametro dizendo que a query possui um "ou" e que esse ou vai buscar pelo campo "id"
**String name** = o parâmetro para a query
**Integer id** = o parâmetro para a query

### **Ordenação:**

exemplo:

```java
List<Client> findByNameOrIdOrderById(String name, Integer id);
```

onde
**find** = dizemos que a query será do tipo busca (poderia ser save, delete etc)
**ByName** = a query será feita com base na propriedade passada, nesse caso Name
**OrId** = parametro dizendo que a query possui um "ou" e que esse ou vai buscar pelo campo "id"
**String name** = o parâmetro para a query
**Integer id** = o parâmetro para a query
**OrderById** = vai fazer a ordenação dos resultados pelo campo "id"

Usamos o prefixo "**find**" nos nomes do métodos quando queremos buscar uma lista de resultado
quando queremos apenas 1 resultado usamos "**findOne**"
exemplo:

```java
findOneByName(String name)
```

**findOne** = dizemos que a query será do tipo busca de apenas 1 resultado (o parâmetro deve ser único no banco para funcionar direito, como um ID ou nesse caso aqui o nome deve ser único)
**ByName** = a query será feita com base na propriedade passada, nesse caso Name
**String name** = o parâmetro para a query
**se existir só 1 registro esse tipo de query vai retornar ele, se existir mais de 1 vai retornar uma exception**

**os parâmetros devem ser colocados na mesma ordem que você declarou no query method**
exemplo:
**assim**

```java
findOneByNameOrId(String name, Integer id)
```

**e não assim**

```java
findOneByNameOrId(Integer id, String name)
```

### Exists:

convenção para queries de verificação de existência, retorna um booleano
exemplo:

```java
boolean existsByName(String name)
```

onde
**exists** = convenção para dizer que é uma query de verificação de existência
**ByName** = a query será feita com base na propriedade passada, nesse caso Name
**String name** = o parâmetro para a query

### Mostrar SQL no console

Quando estamos usando o hibernate (JPA), vamos exibir qual SQL está sendo rodado no console.
Para fazer isso vamo direto no application.properties e adicionamos as propriedades
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true

Essas propriedades diz ao JPA que ele deve printar e formatar os sql quando forem rodados na aplicação

## Criando query string

Criamos queries mais personalizadas, podendo trocar o nome do método e não usar a padronização dos query methods, para fazer isso usamos a anotação @Query(name) e usamos o HQL
exemplo:

```java
@Query(value = "select c from Client c where [c.name](http://c.name/) like :name")
List<Client> podeSerQualquerNome(@Param("name") String name);
```

Aqui a anotação **@Param** dentro dos parâmetros do método serve para dizer a query quais parâmetros passados ao método são parâmtros da query, pore exemplo na query temos o parâmetro ":name" e o **@Param("name")** serve para dizer ao método qual parâmetro da lista de parâmetros passados é esse da query

Também podemos usar SQL nativo ao invés do HQL no "**value**" da **@Query** juntamente com o valor "**nativeQuery**" sendo igual a true
exemplo:

```java
@Query(value = " select * from Client c where [c.name](http://c.name/) like '%:name%' ", nativeQuery = true)
List<Client> buscaporNomeSql(@Param("name") String name);
```

Podemos executar qualquer operação sql, fazer um delete, um update tanto faz, tudo pela anotação **@Query**
Porém um método que não seja de consulta (**insert, update, delete**) deve ser avisado ao sprint que é uma transação utilizando a anotação **@Modifying**
exemplo:

```java
@Query("delete from Client c where [c.name](http://c.name/) = :name")
@Modifying
public void deletarPorNome(@Param("name") String name)
```

## Relacionamentos em JPA

Para referenciar uma outra entidade (chave estrageira) no JPA basta usar a anotação **@ManyToOne** (onde tenho muitas "**entidades atual**" para um "**entidade de destino**") e a anotação **@JoinColumn(name = "nomeDaColunaDeIdDoAlvo")**, esse **@JoinColumn** é quem diz qual coluna é a referência da outra table, por exemplo:

```java
public class Purchase {
	@ManyToOne
	@JoinColumn(name = "CLIENT_ID")
	private Client client;
}

```

onde temos muitas **compras** para um **cliente** (**ManyToOne**) e a coluna usada como foreign key do cliente é a **CLIENT_ID**

### Um para muitos

```java
@OneToMany(mappedBy = "client")
private Set<Purchase> purchases;
```

Pnde esse **mappedBy** serve para dizer quem possui as chaves para esse elemento one, por exemplo o cliente não guarda os pedidos mas a table de pedidos guarda quais pedidos o cliente fez, então esse mappedBy utiliza a o nome da propriedade que está no objeto que gera a table de pedidos, por isso ela usa "**client**" pois na classe **Purchase.java** a propriedade **private Client client** é quem é a referência da table de pedidos para qual cliente fez o pedido

### Definindo ponto flutuante em JPA

```java
@Column(precision = 20, scale = 2)
private BigDecimal total;
```

onde "**precision**" é a quantidade de casas do número e "**scale**" a quantidade de casas após a vírgula

## Fazendo consultas com relacionamentos

Existem duas formas, a primeira é alterando o fetch de um relacionamento de **LAZY** para **EAGER**, assim a cada consulta serão trazidos juntos seus relacionamentos, por exemplo uma relação OneToMany de um client serão trazidos as relações de pedido com cliente, exemplo:

```java
@OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
private Set<Purchase> purchases;
```

apeas de **EAGER** não ser recomendada pois sempre vai trazer os dados, as vezes isso não é necessário, pois deixa mais lento.

A segunda forma é criando uma query, com join mesmo. Exemplo:

```java
@Query(value = " select c from Client c left join fetch c.purchases where [c.id](http://c.id/) = :id ")
Client findClientFetchPedidos(@Param("id") Integer id);
```

## **Entendendo melhor o uso da annotation @Transactional**

Bem, primeiro temos que entender que o básico do uso de transações é que elas devem ser utilizadas toda vez que for necessário fazer modificações no banco de dados (**INSERT, UPDATE e DELETE**), e na otimização de **Queries** (no caso com **readonly=true**), ok?

Um outro detalhe é que, por padrão, os JpaRepositories já introduzem essa annotation nos métodos que executam essas operações não tendo a necessidade explicita de fazer isso, por isso você usa eles e não ocorre erro, mas se vc tentar usar o **EntityManager** diretamente pra fazer as operações e não usar **@Transactional** ao fazer alterações no banco, vai dar erro de transação.

Com isso em mente vemos que, utilizando JpaRepositories, ao fazer 1 única operação de escrita no banco, não é necessário inserir essa annotation, então ela só é obrigatória de fato em algumas situações e eu vou te dar exemplos das que já fiz na prática:

*Cenário 1: Tenho um método no meu serviço que salva um pedido, os itens do pedido e a forma de pagamento.*

Nesse cenário temos 3 entidades envolvidas, logo teremos pelo menos 3 Repositorios diferentes fazendo uma unica operação: `salvarPedido`, nesse caso devemos obrigatóriamente adicionar o **@Transactional**, pois se houver uma exception na hora de salvar o pagamento, por exemplo, o Spring fará um rollback e não persistirá nada que foi feito desde o inicio da operação, ou seja, mesmo que tiver havido sucesso ao salvar o pedido e os itens, eles não estarão persistidos pois ocorreu um erro ao salvar o pagamento e quando vc usa o **@Transactional** nesse método você está indicado que quer fazer tudo em uma unica transação, seguindo assim o principio do ACID.

```java

 @Transactional
public void salvarPedido(Pedido pedido){    
     pedidoRepository.save(pedido);    
     itemPedidoRepository.save(pedido.getItens());    
     pagamentoRepository.save(pedido.getPagamento());
 }
```

Você pode utilizar este mesmo raciocínio quando houver qualquer outra operação dentro daquele método que se der erro, deve ser feito um rollback, como por exemplo no cenário onde vc precisa cadastrar um cliente, depois mandar a foto dele para um bucket na nuvem e por fim enviar um email confirmando o cadastro do mesmo. Neste caso, imagine que o cliente foi persistido no banco, mas ocorreu um erro ao tentar salvar a foto dele no bucket, você não quer que o cliente fique sem a foto, correto? Use o **@Transactional**.

No caso do email, existe aqui uma opção.. digamos que salvar o cliente e a foto são necessários no cadastro e o email é opcional, não vai fazer diferença, é apenas um informativo e não é requisito imprescindível, nesse caso você coloca um `try/catch` apenas na parte do email, retornando apenas uma mensagem talvez, dizendo que a operação ocorreu com sucesso, entretanto não foi possivel enviar o email, e com esse try catch, você impede de este método lançar uma exception, evitando assim o rollback das informações persistidas.

*Cenário 2: Tenho muitas entidades na aplicação e de bancos de dados distintos.*

Imagine uma aplicação que faz leitura de bases Oracle e MySQL, os dados dos funcionários na base MySQL e os dados da empresa na base Oracle (acredite, isso não é tão incomum).

Neste caso a annotation **@Transactional** pode ser obrigatório em quase todos os métodos de persistência, senão poderá ocorrer problemas. Quando eu tenho mais de uma conexão com banco de dados, eu tenho mais de um TransactionManager registrado no contexto do Spring, um para o Oracle (transactionManagerOracle) e um para o MySQL (transactionManagerMySQL), dessa forma, quando eu vou executar uma operação de escrita, por exemplo, de funcionário, eu preciso inserir acima do método salvarFuncionario, a annotation **@Transactional** com o nome do transactionManager, para que o spring consiga identificar para que banco de dados deve abrir transação:

```java

@Transactional("transactionManagerMySQL")
public void salvarFuncionario(Funcionario fun){    
    funcionarioRepository.save(fun);
}
```

Obs: para este tipo de configuração de multiplas conexões, é necessário configurar o transactionManager de cada uma nas suas configurações.

## Criando controllers rest

Primeiramente precisamos ter a dependência do spring-boot-starter-web no pom.xml
exemplo:

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

**@Controller**: Um Bean gerenciado do spring, que é o controller para poder responer um request, ele é gerenciado pelo container de injeção de dependência do spring. Essa anotação é utilizada em uma classe que vai ser o controller

**@RequestMapping("urlBase")**: Define qual a rota ele vai responder, em que a "urlBase" é a rota que o **@Controller** vai responder quando for chamada
Em um método, usamos a **@RequestMapping** da seguinte forma:

```java
@RequestMapping(value = "/hello/{name}", method = RequestMethod.GET) 
```

onde "**value**" é a url que vai completar a url do **RequestMapping** da classe **@Controller**, e nesse caso "**{name}**" é como definimos variáveis que vão vir pela url do request.

**@PathVariable("name")**: usado nos parâmetros de métodos que usam **@RequestMapping**, para dizer qual o nome do parâmetro deve ser colocado na função, junto com o "method" que diz qual método vai bater nessa função

**@ResponseBody**: Diz que o que a função retornar deve ser colocado no corpo das responsta do request

Exemplo de tudo isso

```java
@Controller
@RequestMapping("/api/clients")//definição de url base para acessar esse controller
public class ClientController {
	@RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
//path final que vai receber um nome dps do último "/" e jogar para dentro da função como parâmetro "nameClient"

	@ResponseBody 
//aqui o "return String.format("Hello %s", nameClient);" vai ser colocado no response.body
	public String helloClient(@PathVariable("name") String nameClient){
	    return String.format("Hello %s", nameClient);
	}
}
```

**@RequestBody**: Anotação utilizada nos parâmetros do método que define em qual parâmetro vai ser colocado o corpo da requisição

Notas:
**@RequestMapping(value = {"/hello/{name}", "/teste/"}, method = RequestMethod.GET)** o atributo "**value**" pode receber mais de 1 valor, ele recebe um array de strings e todas elas vão bater na função que ele está anotando, mas tendo que ser o mesmo método "GET"

**@RequestMapping(value = {"/hello/{name}"}, consumes= {"application/json", "application/xml"})**
"**consumes**" define quais MIME types vão ser aceitos naquele resquest (os **content-types**), ele também recebe um array de strings

**@RequestMapping(value = {"/hello/{name}"}, produces= {"application/json", "application/xml"})**
"**produces**" é o mesmo que o "**consume**" mas para resposta, ele diz o que nós aceitamos enviar como resposta para o usuário, também recebe um array de strings

**@GetMapping:** semelhante ao **@RequestMapping** mas não precisamos dizer qual o method pois ela funciona direto para GET

> NOTA: Qualquer método com a anotação **@ResponseBody** deve retornar um objeto, se não tiver essa anotação DEVE retornar uma string com o path de qual html ou seja lá oq for ele deve acessar , caso contrário da erro
> 

**ResponseEntity**: Classe de respostas para métodos, por exemplo podemos responder com um 200 (ok) e com os dados do request da seguinte forma:

```java
ResponseEntity.ok(new Client("bla"));
```

ou dizer que não foi encontrado com

```java
ResponseEntity.notFound().build(); 
//o que vem antes do build é só um "constructor"
```

também podemos instanciar essa classe da seguinte forma

```java
ResponseEntity<Client> responseEntity = new ResponseEntity<>(new Client("Bla"), HttpStatus.OK);
```

ambos são iguais, mas o contrutor pode ser bem útil para customizar a resposta

**@PostMapping**: Anotação usada apra dizer que o método vai ser executado quando bater um post na rota especificada
**@RequestBody**: anotação utilizada nos parâmetros do método para dizer que aquele parâmetro vai ser ingetado com o body que vier no request
exemplo:

```java
@PostMapping
@ResponseBody
public ResponseEntity createClient(@RequestBody Client client){}
```

**@JsonIgnore**: Anotação utilizada nas entities para sinalizar ao spring que aquele campo deve ser ignorado na hora de responder a um request (ignorado = não enviado)

**@DeleteMapping("/{id}"**): anotação para ser usada em métodos que devem responder a request feitos com o verbo DELETE que correspondem a url passada entre parenteses

### ExampleMatcher

Cria um "comparador" para ser usado em consultas, por exemplo podemos criar um comparador que o resultado bata com o valor a ser comparado, que ignore letras maiuscular e minusculas e que observe se a string comparada contem a string ou se começa ou se termina com ela
EXEMPLO:

```java
ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
```

onde: **ExampleMatcher.StringMatcher.CONTAINING** quer dizer que buscamos valores que contenham a string que vai ser passada no Example

**Example**: Do pacote "**org.springframework.data.domain**", serve para montar como se fosse uma **query** para ser usado junto do **findAll** dos repositories, ai o repository faz a pesquisa levando em conta essa estratégia do Example que foi passada
exemplo:

```java
ExampleMatcher matcher = ExampleMatcher
.matching()
.withIgnoreCase()
.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
Example example = Example.of(filtro, matcher);
List<Client> clientsFounded = clients.findAll(example);
```

## RestController

**@RestController**: Anotação especializada para controllers rest, já vem com a anotação **@ResponseBody** ai não precisamos usar a anotação @ResponseBody em cima de cada método
exemplo de uso:

```java
@RestController
@RequestMapping("/api/clients")
public class ClientController {}
```

Retornando status code e entidades ao invés do **ResponseEntity**
Podemos retornar a entidade normalmente quando ela é encontrada, vide o exemplo:

```java
@GetMapping("/{id}")
public Client getClientById(@PathVariable Integer id) {
Optional<Client> client = clients.findById(id);
if (client.isPresent()) {
        return client.get();
    }
}
```

porém quando não encontramos um dado ou queremos indicar um erro, jogamos uma exception do tipo "**ResponseStatusException**" da seguinte forma 

```java
throw new ResponseStatusException()
```

Serve para retornar mensagens de erro para o cliente
exemplo

```java
throw new ResponseStatusException(HttpStatus status, String msg)
throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deu erro aqui vai sua msg")
```

**@ResponseStatus**: Anotação utilizada para definir um httpStatus padrão a ser retornado pelo método, qual o status deve ser retornado se tudo correr bem
exemplo:

```java
@ResponseStatus(HttpStatus.CREATED)
public Client createClient(@RequestBody Client client) {
	return clients.save(client);
}
```

### import estatico

import static biblioteca.*
exemplo:

```java
import static org.springframework.http.HttpStatus.*;
```

serve para você não precisar ficar importando de um em um e listando todos com HttpStatus antes por exemplo

## **Entidades complexas**

Entidades que tem dependência de outras tables, é bom criar uma classe de serviços, "**services**". Cada serviço deve ser uma interface e deve ter também uma class que o implementa. Pq deve ser interface? para quando formos testar podermos usar objetos do tipo mock.

**@Service**: estereótipo do spring usado para anotar um service

**DTO**: Data transfer object,
serve para mapearmos objetos com propriedades simples, para ser recebido via requisição e ser transformado no modelo de dado correto

## Lombok

Biblioteca que pode ser utilizada com a dependency

```xml
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<version>1.18.22</version>
	<scope>provided</scope>
</dependency>
```

instalar o plugin do lombok para linkar com a IDE (atraves do plugin do lombok da sua ide)
serve para a ide identificar os getters e setters da classe sem precisar a gente declarar eles, o lombok coloca esses códigos mais padrão sozinhos
utilizando as anotações **@Getter** e **@Setter** do Lombok

exemplo

```java
@Getter
@Setter
public class ItensPurchaseDTO {
	private Integer productId;
	private Integer amount;
}
```

temos outras anotações como **@ToString** que implementa um **toString** na classe, **@NoAragsContructor** para gerar construtores sem argumentos, **@AllArgsConstructor**
para gerar construtores com todas as propriedades da classe
Também temos a **@Data** que é um compilado de algumas dessas anotações
Essas anotações não interferem em vc criar mesmo os getter, setters e constructors

**@RequiredArgsConstructor:**
cria um construtor com todos os atributos requeridos, que nesse caso são declarados como final, exemplo

```java
@RequiredArgsConstructor
public class PurchaseServiceImplementation implements PurchasesService {
	private final Purchases purchases;
	private final Clients clients;
	private final Products products;
}
```

### Spring boot dev tools

Serve para facilitar nossa vida, toda a vez que compilarmos uma classe (salvar) o dev tools vai subir a aplicação novamente pra gente, sem a necessiadade de
ficar fazendo isso manualmente.
Podemos instalar através da dependency

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-devtools</artifactId>
</dependency>
```

### Tratamento de exceptions

**@ControllerAdvice**: Controlador que permite fazermos tratamentos usando exceptionHandlers, que são metodos que quando um erro é lançado eles que captura,
então com eles podemos pegar esses erros e retornar uma mensgem

**@ExceptionHandler:**
Marca a função para ela ser um tratador de erros, precisamos dizer qual exception vamos tratar passando nos parenteses dessa anotação a classe da exception, exemplo:

```java
@ExceptionHandler(RuleException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
private APIErrors handleRuleException(RuleException ex) {
	String errMessage = ex.getMessage();
	return new APIErrors(errMessage);
}
```

Toda vez que o projeto lançar essa exception, ele vai cair dentro desse método como tratamento, não importa aonde lançar a exceptio cai aqui

**@RestControllerAdvice**: Mesma coisa que a **@ControllerAdvice**, porém todas as suas funções já tem o **@ResponseBody**, caso você use a **@ControllerAdvice** vai
precisar marcar todas as funções com @ResponseBody para dizer que elas retornam a resposta do método

### Builder

**@Builder**: anotação do lombok utilizada para gerar um builder na classe, esse builder serve para criarmos um objeto sem precisar instanciar, vamos apenas
setando os valores que queremos naquele objeto, exemplo:

```java

InfosPurchaseDTO
.builder()
.code(purchase.getId())
.nameClient(purchase.getClient().getName())
```

isso vai retornar um InfosPurchaseDTO, classe que foi anotada com **@Builder**

## Enumerated

**@Enumerated**: Serve para dizer que aquele tipo de dado é um enum, pois ele não existe no banco de dados (não é primitivo)
e que queremos salvar ele como uma string
**EnumType**: Serve para dizer se queremos que a anotação @Enumerated vai gravar a string do valor, ou a posição do valor
exemplo de uso:

```java
**@Enumerated(EnumType.STRING)**
private StatusPedido status;
```

vai gravar no banco a string do enum selecionada, não sua posição no array de enums

### Bean validation

Dependency para utilizar o bean validation

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**@NotNull**: anotação utilizada para dizer a validação que o campo não pode ser nulo (null) exemplo de uso:

```java
@Column(precision = 20, scale = 2)
@NotNull
private BigDecimal price;
```

**@NotEmpty**: anotação utilizada para dizer que a validação que o campo não pode ser vazio, exemplo de uso:

```java
@Column(name = "name", length = 100)
@NotEmpty(message = "O campo nome não pode ser vazio")
private String name;
```

**@Valid**: Anotação utilizada em assinaturas de métodos, serve para validar a entidade (parâmetro) que está chegando via **@RequestBody**
exemplo:

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public Client createClient(@RequestBody @Valid Client client) {
	return clients.save(client);
}
```

### Criando anotações de validação personalizadas

Devemos criar uma nova interface do tipo **@interface**, essa interafce deve ter as seguintes anotações
**@Retention(RetentionPolicy.RUNTIME):** Que diz que a interface deve ser escaneada pelo spring boot, o valor "RetentionPolicy.RUNTIME", diz que ela vai ser
escaneda assim que o spring iniciar, existem outras variações de quando ela deve ser escaneada mas essa é mais comun.
**@Target(ElementType.FIELD)**: Dizemos aonde essa validação vai ser usada, se ela pode ser usada em cima de métodos, de classes inteiras ou nesse caso, de campos, por isso o modificador **"ElementType.FIELD"**
**@Constraint(validatedBy = NotEmptyListValidartor.class)**: diz aonde está a implementação da nossa anotação, nesse caso a classe "**NotEmptyListValidartor**"
é onde a validação é implementada efetivamente

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NotEmptyListValidartor.class)
public @interface NotEmptyList {
	//mensagem padrão do validator
	String message() default "A lista não pode ser vazia";
	//métodos que DEVEM ser implementados
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
```

Exemplo de implementação da classe que efetivamente implementa a validação

```java

public class NotEmptyListValidartor implements ConstraintValidator<NotEmptyList, List>{

	@Override
	public void initialize(NotEmptyList constraintAnnotation){}

	@Override
	public boolean isValid(List value, ConstraintValidatorContext context) {
	    // aqui é onde montamos a validação que queremos
	    return false;
	}
}
```

a classe deve implementar "**ConstraintValidator**", que recebe a **@interface (NotEmptyList)** e em que tipo de dado essa validação vai rodar (List)

### Internacionalização

Dentro da pasta resources (mesma pasta de application.properties) criar um arquivo chamado **messages.properties** e colar o conteúdo das mensagens dentro
do arquivo com chave e valor, agora para dizer ao spring que deve fazer a leitura desse arquivo e usar suas chaves
exemplo:

```xml
com.client-code-mandatory=O código do cliente é obrigatório
com.product-description-mandatory=Campo description não pode ser vazio
com.product-price-mandatory=Campo price não pode ser vazio
com.pruchase-dto-client-code-mandatory=O código do cliente não pode ser vazio
```

para referenciar a mensagem dentro do bean usamos a seguinte notação
"**{com.client-code-mandatory}**"
exemplo completo
**@NotEmpty(message = "{com.client-name-mandatory}")**

para usarmos isso, no pacote raiz criamos o pacote config e dentro dele a classe InternacionalizacaoConfig
essa classe deve ser anotada com **@Configuration** para dizer ao spring que é uma classe de configuração
exemplo que deve ser a implementação dessa classe

```java
@Configuration
public class InternacionalizationConfig {
	@Bean
	public MessageSource messageSource(){
		    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	
	    messageSource.setBasename("classpath:messages");
	    messageSource.setDefaultEncoding("ISO-8859-1");
	    messageSource.setDefaultLocale(Locale.getDefault());
	    return messageSource;
	}
	
	@Bean
	public LocalValidatorFactoryBean validatorFactoryBean(){
	    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	
	    bean.setValidationMessageSource(messageSource());
	    return bean;
	}
}
```

### Spring boot security

Dependência necessária para usar o Spring boot security

```xml
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Ao adicionar essa dependência, já vai ser criado um sistema próprio de login do SBS, que vai gerar uma senha e mostrar ela no terminal, para
usarmos os endpoints dai pra frente devemos fazer login para a rota "[http://localhost:8080/login](http://localhost:8080/login)" com o seguinte form:

```json
{
username:user,
password: aSenhaGeradaNoTerminal
}
```

### Classe de config do security

Dentro do pacote config (mesmo pacote da internacionalização) criamos uma classe chamada SecurityConfig (pode ser qualquer nome) ela que vai ter toda a config
do spring security que deve extender a classe "**WebSecurityConfigurerAdapter**"
A primeira anotação utilizada nessa classe é
**@EnableWebSecurity**
Nessa classe não precisamos usar a **@Configuration**, pois essa anotação **@EnableWebSecurity** já cobre isso.
Nessa classe criada vamos sobreescrever dois métodos, o

```java
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	super.configure(auth);
}
```

e

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
	super.configure(http);
}
```

O primeiro método (**configure(AuthenticationManagerBuilder auth)**) serve para trazer os objetos que vão fazer as autenticação dos usuários e adicionar os usuários dentro do security, é basicamente onde ocorre a autenticação mesmo. De onde vamos buscar o usuário e a senha para comparar por exemplo

Já o segundo (**configure(HttpSecurity http)**), é a parte de autorização, é aqui onde eu verifico o usuário que vem no request e vejo se ele tem autorização para acessar  determinado endpoint, onde podemos verificar as roles dos users por exemplo

O **AuthenticationManagerBuilder** precisa de um **Password encoder**, que é montado nessa função a seguir, retornando o encoder e implementado nele nossas regras
de codificação dos dados do usuário.
O sprign já tem um pacote de **PasswordEncoded**, o **BCryptPasswordEncoder**. Então podemos gerar a função encode do spring utilizando a seguinte declaração

```java
public PasswordEncoder encoder(){
	return new BCryptPasswordEncoder();
};
```

### Configurando a autenticação

Nesse caso estamos configurando uma autenticação em memória com o usuário RAFAEL senha RAFAEL, hardcoded mesmo, mas é só para exemplo não é assim que
configuramos com o banco de dados

```java

@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.inMemoryAuthentication()
	.passwordEncoder(encoder())
	.withUser("rafael")
	.password(encoder().encode("rafael"))
	.roles("USER");
}
```

### Criando autorização das roles

Isso é feito no método **configure(HttpSecurity http)** do seu **SecurityConfig**
Primeiro desabilitamos o csrf do HttpSecurity da seguinte forma

```java
http.csrf().disable();
```

O csrf serve para manter uma config que permite que haja segurança entre uma aplicação web (front) e o backend, nesse caso não vamos usar isso pois somos
uma api REST então vamos usar o modo stateless

Temos algumas opções de regras que podem ser implementadas para acessos a urls
**1: Limita o acesso a Roles**

```java
http.csrf().disable()
.authorizeRequests()
.antMatchers("/api/clients/**").hasRole("USER");
```

**2: Limita o acesso a authority, são como as permissões dentro das roles**

```java
http.csrf().disable()
.authorizeRequests()
.antMatchers("/api/clients/**")
.hasAuthority("MANTAIN_USER");
```

**3: Libera o acesso para todos**

```java
http.csrf().disable()
.authorizeRequests()
.antMatchers("/api/clients/**")
.permitAll()
```

**4: Libera a rota para todos, desde que esteja autenticado**

```java
http.csrf().disable()
.authorizeRequests()
.antMatchers("/api/clients/**")
.authenticated()
```

### Criando o form de login

Usamos a função .formLogin() do objeto HttpSecurity, dessa forma ele cria automaticamente a tela de login padrão do spring security

```java
http.csrf().disable()
.authorizeRequests()
.antMatchers("/api/clients/**")
.authenticated()
.and()
.formLogin();
```

ou caso você cria um form padrão, basta colocar o path dele (que está dentro de resources, na pasta static ou public ou templates)
dentro da função **formLogin()**, exemplo:

```java
.formLogin("/login.html")
```

esse form deve ser method post e ter 2 campos de input o "username" e o "password" com esses "name" mesmo

### Adicionando banco de dados na autenticação

Criar uma classe para implementação de entendidade e service, no caso UserService e implementar a interface "**UserDetailsService**". Serve para
definir o carregamento de usuários através de uma base de dados

### JWT (Json web token)

O jwt é formado por 3 partes, o header, o payload e a verify signature. Todas essas partes são separadas por "." é codificado em BASE64
O header serve para verificar o algoritmo e o tipo do token
o Payload serve para efetivaente enviar os dados do token, como usuário, role etc etc. Pode ser qualquer coisa, você que escolhe
A verify signature serve para validar a chave de assinatura (que só o servidor sabe) e serve para você decodificar o token

dependency para instalar o jjwebtoken

```xml
<dependency>
<groupId>io.jsonwebtonke</groupId>
<artifactId>jjwt</artifactId>
<version>0.9.1</version>
</dependency>
```

A primeira coisa a ser feita é criar uma classe (**JWTService**) que vai usar essa biblioteca, que vai servir para codificar as credenciais (não a senha)

Após isso para facilitar, criamos 2 **application.properties** para guardar o tempo de expiração do token e a sua chave de assinatura, então no arquivo resources/application.properties criar:

```xml
security.jwt.expiration=30
security.jwt.signatureKey=suaChave
```

A geração de token é feita com a classe Jwts usando a função

```java
builder() 
.setSubject(user.getUsername())//seta o subject do token, a parte de dados no caso, o que queremos enviar
.setExpiration(date) //seta a data de expiração do token, deve ser sempre um Date
.signWith(SignatureAlgorithm.HS512, this.sigantureKey) //define o algoritmo a ser usado para codificar o token e a chave a ser usada
.compact();//cria tudo isso em uma string
```

Também podemos definir demais dados no corpo do token através de claims, que são um hashmap de String e Object exemplo:

```java
    
HashMap<String, Object> claims = new HashMap<>();
claims.put("email", "[teste@email.com](mailto:teste@email.com)");
claims.put("roles", "ADMIN");
return Jwts
            .builder()
            .setSubject(user.getUsername())
            .setExpiration(date)
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, this.sigantureKey)
            .compact();

```

onde a var "**claims**" contem todos os outros dados que vamos enviar no token e a função "**setClaims(claims)**" coloca esses dados no token

### Decodificando tokens

Usamos o objeto JWT, com sua função parser para decodificar um token em string, exemplo

```java
public Claims getClaims(String token){
	return Jwts
	.parser()
	.setSigningKey(this.sigantureKey) //colocamos qual foi a assinatura que usamos para codificar o token
	.parseClaimsJws(token) //passamos a string do token para fazer essa decodificação
	.getBody(); //retorna as Claims (dados) contidos no token
}
```

### Implementando os tokens no login

Criamos uma classe que extenda "**OncePerRequestFilter**" e sobreescrevemos o método, doFilterInternal, nele verificamos se o token que vem nos **header (authorization)** do request estão ok
Esse filtro intercepta uma request (req, res e filter) e obtem as infos que você quiser e antes de processar o request estamos adiconando um user autenticado
na sessão se o token estiver ok
Registrando esse filtro ao spring:
Dentro do seu **@EnableWeSecutiry**, devemos registrar um bean para esse filtro
exemplo:

```java

@Autowired
private JWTService jwtService;
@Autowired
private UserServiceImplementation serviceImplementation;
@Bean
private JWTAuthFilter fAuthFilter(){
    return new JWTAuthFilter(jwtService, serviceImplementation)
}

```

após isso na função de config, deixamos de usar httpBasei e passamos a user **SessionCreationPolicy.STATELESS** da seguinte forma

```java

http.csrf().disable()
.authorizeRequests()
.antMatchers("/api/clients/**")
.hasAnyRole("USER", "ADMIN")
.antMatchers("/api/products/**")
.hasRole("ADMIN")
.antMatchers("/api/pedidos/**")
.hasAnyRole("USER", "ADMIN")
.antMatchers(HttpMethod.POST,"/api/users/**")
.permitAll()
.anyRequest()
.authenticated()
.and()
.sessionManagement()
.sessionCreationPolicy(SessionCreationPolicy.STATELESS)//dizemos que a sessão é stateless, ou seja td o request tem que vir com
//um token para poder ser autenticado, se não não passa
.and()
.addFilterBefore(this.fAuthFilter(), UsernamePasswordAuthenticationFilter.class);
//dizemos qual filtro vai rodar (o nosso (this.fAuthFilter) e antes de qual filtro padrão do sistema (UsernamePasswordAuthenticationFilter.class nesse caso)
```

### Conectando ao Mysql

Primeiro devemos criar (manualmente mesmo) o banco de dados:

```sql
create database nomeDoSeuBancoDeDados
```

após a criação, adicionamos as seguintes propriedades no **application.properties**

```xml
spring.datasource.url = jdbc:mysql://localhost:3306/springLearn?useTimeZone=true&serverTimezone=UTC
```

onde
"**mysql://localhost:3306/**" endereço localhost do serviço de banco de dados mysql
"**springLearn**" nome do banco que queremos nos conectar
"**useTimeZone=true**" dizemos ao banco de dados para usar o timezone que vamos passar
"**&serverTimeZone=UTC**" diz para usar o timezone passado, nesse caso UTC

```xml
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

que é o driver para conexão com banco de dados mysql, essa classe vem da dependência da classe mysql
dependência para usar a conexão com mysql

```xml
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
```

```xml
spring.datasource.username=seuUserDoBanco
spring.datasource.password=suaSenhaDoBanco
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

dialeto do mysql, para ser usado junto do JPA, é o nosso ORM

caso de erro:
**Table 'springlearn.hibernate_sequence' doesn't exist**
use

```xml
spring.jpa.hibernate.use-new-id-generator-mappings= false
```

pois o mysql não está sabendo em qual table iniciar a geração de ids auto_increments, basicamente ele está se perdendo
ou troque todos os seus "**GenerationType.AUTO**" por "**GenerationType.IDENTITY**"

### Documentação de API (swagger)

A documentação do spring usa o swagger, que precisa de duas dependências:

```xml
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.9.2</version>
</dependency>
```

e

```xml
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.9.2</version>
</dependency>
```

após isso criamos uma classe de config para o swagger, com a anotação **@Configuration** e **@EnableSwagger2**

é necessário criar um **@Bean** de **Docket**, que é uma função que retorna um **Docket,** exemplo:

```java
@Bean
public Docket docked(){
	return new Docket(DocumentationType.SWAGGER_2)
	.useDefaultResponseMessages(false)
	.select()
	.apis(RequestHandlerSelectors.basePackage("com.springboot.rafael.api.controller")) //dizemos que queremos mapera as api do pacote passado como parm
	.paths(PathSelectors.any())
	.build()
	.apiInfo(this.apiInfo());
}
```

é necessário também criar uma configuração de "**WebSecurity**" para permitir o mapeamento das rotas do swagger pelo spring, exemplo:

```java
public void configure(WebSecurity web) throws Exception{
	web.ignoring().antMatchers(
	"/v2/api-docs",
	"/configuration/ui",
	"/swagguer-resources/",
	"/configuration/security",
	"/swagger-ui.html",
	"/webjars/"
	);
}
```

isso deve ser implementado na sua classe **SecurityConfig** ou equivalente

### Configurando o swagger para poder fazer requests

precisamos configurar uma apikey para ele, através do método

```java
public ApiKey apikey(){
return new ApiKey("JTW", "Authorization", "header");
}
```

criado no seu arquivo Swagger.java

### Customização da UI do swagger

Usamos a anotação **@Api** em cima de controllers, junto com o nome que queremos dar para aquela representação na documentação
exemplo:

```java
@RestController
@RequestMapping("/api/clients")
@Api("Clientes")
public class ClientController {}
```

para mapear métodos é bem similar, usamos a anotação **@ApiOperation("detalhes do método")** e **@ApiResponse(StatusCode)** ou **@ApiResponses({
@ApiResponse(statusCode)
@ApiReponse(StatusCode)
})**

O **@ApiResponses** serve para mápear multiplos status de resposta, exemplo

```java
@GetMapping("/{id}")
@ApiOperation("Obter detalhes de um cliente")
@ApiResponses({
@ApiResponse(code = 200, message = "Cliente encontrado"),
@ApiResponse(code = 404, message = "Cliente não localizado")
})
public Client getClientById(@PathVariable Integer id) {
    return clients.findById(id).orElseThrow(

```

**@ApiParam**: vai antes dos parâmetros de métodos, serve para descrever o que é aquele parâmetro, exemplo:

```java
public Client getClientById(@PathVariable @ApiParam("ID do cliente") Integer id) {}
```

## Deploy

### Gerando uma build

É necessário termos o plugin de build configurado no pom.xml

```xml

<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
</build>
```

essa build vai gerar um arquivo .jar
Para buildar abrimos o terminal e digitamos:
**mvn clean package**

onde:
**mvn = maven
clean = para limpar a nossa pasta de target
package = buildar a aplicação com nossas configs do pom**

### Buildando para .war que roda em servers

Para isso devemos adicionar no arquivo pom.xml a seguinte tag

```xml

<packaging>war</packaging>
essa tag vai logo abaixo da tag <version> da sua aplicação exemplo:
<groupId>com.springboot.rafael</groupId>
<artifactId>springboot.rafael</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>war</packaging>
```

Também é necessário adicionar o servidor do tomcat as dependências da aplicação:

```xml

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<scope>provided</scope>
</dependency>
```

a tag <scope>provided</scope> para dizer que esse tom cat vai ser provido, não vai mais ser o da aplicação

Após isso precisamos ir na nossa classe main e dizer que ela extende "SpringBootServletInitializer", agora ela vira uma aplicação full webq que gera um war
exemplo:
@SpringBootApplication
@RestController
public class VendasApplication extends SpringBootServletInitializer{}

Criando profile maven para diferentes builds
para isso criamos uma tag <profiles> dentro do pom.xml, e definimos os prefis do seguinte modo
<profiles>
<profile>
<activation>
<activeByDefault>true</activeByDefault> //dizemos se o perfil é o padrão ou não
</activation>
<id>development</id>//definimos o id do perfil
<properties>
<packaging>jar</packaging> //propriedade criada pela gente, pode ter qualquer nome
<tomcat.scope>compile</tomcat.scope>//propriedade criada pela gente, pode ter qualquer nome
</properties>
</profile>
<profile>
<activation>
<activeByDefault>false</activeByDefault>
</activation>
<id>build</id>
<properties>
<packaging>war</packaging>
<tomcat.scope>provided</tomcat.scope>
</properties>
</profile>
</profiles>
e para usar os valores desse perfil, usamor ${nomeDaPropriedade}
exemplo:
<groupId>com.springboot.rafael</groupId>
<artifactId>springboot.rafael</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>${packaging}</packaging>//esse ${packaging} vai ser substituido sempre pelo <packaging> do nosso perfil

ai para buildar com um perfil que não seja o padrão, rodamos com -P nomeDoProfile
mvn clean package -P build

### Spring initializer

[start.spring.io](http://start.spring.io/) é o site onde podemos criar um projeto spring boot para posteriormente ser baixado e importado na IDE
Existe um plugin chamado "**Spring Assistant**" que faz a criação dos projetos direto na IDE, procure para a sua IDE

### Rodando SQL no inicio da aplicação

Na pasta **resources**, podemos criar um arquivo com o nome **data.sql**, especificamente esse nome, na pasta **resources**. Através desse arquivos podemos rodar
scripts sql assim que a aplicação sobe, nesse arquivo escrevemos puramente os sql que queremos rodar em ordem de leitura normal (de cima para baixo)
Porém o banco de dados em memória H2 já cria essas tables automaticamente (H2 é só para testes) e para desabilitar isso e podermos rodar o data.sql
precisamos colocar a propriedade "**spring.jpa.hibernate.ddl-auto=none**" no seu arquivo properties
existem os seguintes tipos de ddl-auto
**none = não executa a criação automatica**
**create= cria as tabelas automaticamente sem dropar
drop-and-create= dropa e cria as tabelas automaticamente
update= apenas cria e atualiza as tabelas, sem dropa (igual ao create if not exists)**

nota para definir o valor é chave: valor, tendo um espaço entre o chave: e o valor

### Usando YAML no lugar de application.properties

Criar na pasta resources o arquivo **application.yaml**, possui um formato parecido com json, porém sem {} e é hierarquico.
A hierarquiafunciona da seguinte forma:

```yaml
spring:
	jpa:
		show_sql: true
		hibernate:
			ddl-auto: none
		properties:
			hibernate:
			format_sql: true
```

### Paginação e ordenação de resultados de queries

Para ordenar e/ou paginar uma consulta, passamos uma das seguintes prorpiedades ao final da query
Para ordenar:

```java
public List<Cidade> findByNomeLike(String nome, Sort sort);
```

onde Sort do pacote "**org.springframework.data.domain.Sort**" vai realizar a ordenação

e na chamada do método:

```java
cidadeRepository.findByNomeLike("c", Sort.by("habitantes"))
```

Onde **Sort.by(String)** recebe o nome (em string) da propriedade que vai ser usada para ordenar, pode ser mais de uma propriedade, basta passar como

```java
cidadeRepository.findByNomeLike("c", Sort.by("habitantes", "nome"))
```

Para paginar, adicionamos um parâmetro Pageable ao final da query, vindo do pacote "**org.springframework.data.domain.Pageable**"

```java
public List<Cidade> findByHabitantesGreaterThan(Long habitantes, Pageable pageable);
```

e na chamada da função passamos um **Pageable** criado por **PageRequest.of(int primairePage, int ocorrenciasPorPage)** como no exemplo

```java
Pageable pageable =  PageRequest.of(0, 10);
cidadeRepository.findByHabitantesGreaterThan(12313L, pageable).forEach(System.out::println);
```

onde o 0 diz que vai iniciar da página 0, e o 10 que teremos 10 elementos por página

### Query by example

Para criar uma query que usa example, criamos um objeto do tipo **Example<TipoQueVamosBuscar>**, esse
Example é do pacote "**org.springframework.data.domain.Example**"
exemplo de uso:

```
  
public List<Cidade> filtroDinamico(Cidade cidade){
	Example<Cidade> example = Example.of(cidade);
  return cidadeRepository.findAll(example);
}

```

onde o **findAll** vai usar o example para procurar os elementos que batem com os dados passados por ele (o example). Assim caso você passe null em uma
das propriedades do objeto usado para filtro, não vai dar erro
Também podemos criar um **ExampleMatcher**, para filtrar melhor os resultados, por exemplo
**ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase()**
cria um **ExampleMatcher** que ignora o case (maiúscula ou minúscula) das strings, existem diversas opções
exemplo de uso:

```java
ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase();
Example<Cidade> example = Example.of(cidade, matcher);
```

### Specifications

Para utilizar Specifications, extendemos a classe JPASpecificationExecutor<TipoDeElemento>
exemplo:

```java
public interface CidadeRepository extends JpaRepository<Cidade, Integer>, JpaSpecificationExecutor
```

Uma specification é um pedaço de query, onde juntamos várias specifications para montar uma query, [link para estudos do criteria builder](https://www.objectdb.com/java/jpa/query/criteria)

### Criando queries nativas (SQL)

Usamos a anotação **@Query** logo acima do método, com a flag **nativeQuery = true**, exemplo:

```java
@Query(nativeQuery = true)
public List<Cidade> findByNomeSQL(String nome);
seguindo da propriedade value = suaQuery
exemplo completo>
@Query(nativeQuery = true, value = "select * from Cidade as c where c.nome =:nome")
public List<Cidade> findByNomeSQL(@Param("nome") String nome);
```

### Projections

Serve para retornar valores específicos de queries sql nativas
Para isso criamos uma interface contendo o que queremos que a query retorne como por exemplo:

```java
public interface CidadeProjections {
	Integer getId();
	String getNome();
}
```

e usamos da seguinte forma na query:

```java
@Query(nativeQuery = true, value = "select * from cidade as c where c.nome =:nome")
public List<CidadeProjections> findByNomeSQL(@Param("nome") String nome);
```

### Criando imagem docker de uma aplicação spring

Precisamos primeiro buildar a aplicação em jar ou war para termos um "executavel" para a jvm
fazemos isso através do comando "**mvn clean package**"
isso vai gerar nosso arquivo .jar dentro da pasta target que para executar podemos rodar 

```bash
java -jar /target/nomeDoJar.jar
```

para rodar a aplicação dentro de uma imagem docker vamos até a raiz do projeto e criamos um arquivo chamado: **Dockerfile**
sem extensão mesmo. O dockerfile serve para gerar imagens docker que servem para rodar containers
contendo dentro desse docker file as seguintes instruções:

```docker
FROM openjdk:8 //definimos a imagem base, no nosso caso o java 8
WORKDIR /app //definimos qual vai ser o diretorio que vamos executar comando a partir daqui
COPY ./target/*.jar ./app.jar //copiamos a build .jar da nossa aplicação para dentro de /app (definidono comando anterior) com o nome app.jar (pode ser qualquer nome)
EXPOSE 8080 //expomos a porta que queremos que o docker fique escutando (por padrão é a mesma da aplicação, nesse caso 8080)
ENTRYPOINT java -jar app.jar //usamos esse comando para rodar efetivamente a .jar dentro do container
```

Após isso, para buildar a imagem rodamos o comando 

```bash
docker build -t suaTag pathParaOArquivoDockerfile
```

exemplo:

```bash

docker build -t rafaelwassoaski/localizacao:1.0.0 .
```

a tag vai ser "**rafaelwassoaski/localizacao:1.0.0**" e o **.** pois estou rodando esse comando na mesma pasta que está o arquivo Dockerfile

a partir da imagem buildada, usamos o comando

```bash
docker run --name nomeDoContainer -p PortaQueVaiRodarNaSuaMaquina:PortaDoContainerQueVoceExpos tagDaImage
```

 para iniciar um container com aquela imagem
exemplo

```bash
docker run --name SpringDockerMeuNome -p 8081:8080 rafaelwassoaski/localizacao:1.0.0
```

para parar um container basta rodar docker stop nomeDoContainer
exemplo:

```bash
docker stop SpringDockerMeuNome
```

para rodar novamente esse container pausado, basta rodar docker start nomeDoContainer
exemplo:

```bash
docker stop SpringDockerMeuNome
```

### Montando a imagem em estágios

É possível automatizar todo o processo de build da aplicação (mvn clean package), fazemos o build diretamente através do Dockerfile
adicionando os seguintes comandos

```docker
FROM maven:3.8.4-jdk-8 as build //definimos um novo estágio para buildar, nomeado como "build" através do comando "as build"
WORKDIR /build //definimos o diretório em que as coisas vão ser rodadas, igual ao estágio anterior
COPY . . //copiamos td do diretório atual (src e pom) para o workdir definido, podemos usar um arquivo .dockerignore criado na pasta raiz da aplicação igual é feito com o .gitignoe, depois coloco ele para exemplo
RUN mvn clean package -D skipTests //usamos o comando RUN para executar algum comando no terminal, nesse caso para buildar
FROM openjdk:8 as run
WORKDIR /app
COPY --from=build ./build/target/*.jar ./app.jar //aqui apenas adicionamos o --from=build para dizer que queremos copiar do estágio anterior chamado buil e colocamos o path completo dele, junto com o seu WORKDIR
EXPOSE 8080
ENTRYPOINT java -jar app.jar
```

Exemplo de .dockerignore, arquivo criado na msm pasta de Dockerfile

```
.mvn
.gitignore
mvnw*
```

### Enviando a imagem para o dockerhub

digite "**docker login**" para realiazer o login com seu nome de usuário e senha no dockerhub
seguido do comando "**docker push tagDaImagem**"
exemplo

```bash
docker push rafaelwassoaski/localizacao:1.0.0
```

Podemos brincar com essa imagem no site docker labs: [https://labs.play-with-docker.com/](https://labs.play-with-docker.com/) logando com seu dockerhub
