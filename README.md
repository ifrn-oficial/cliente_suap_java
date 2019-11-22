# cliente_suap_java
Cliente para obtenção do token de acesso para API do SUAP em aplicações stand-alone Java

# Fluxo
- A aplicação instancia o cliente passando o client_id e client_secret obtidos no cadastro da aplicação no SUAP
- A aplicação executa o método "authorize"
- O sistema operacional abre o browser na tela de autorização do SUAP
- O usuário informa as credenciais
- O browser exibe uma mensagem de sucesso
- A aplicação recebe o token de acesso para realização de chamadas a API do SUAP em nome do usuário

# Exemplo de Uso
```
import br.edu.ifrn.suap.SuapClient;

SuapClient client = new SuapClient("XXXXXXX", "XXXXXXX");
String token = client.authorize();
//o fluxo de execução é interronpido até que o usário realize a autenticação na tela do navegador aberta pela aplicação
System.out.println(token);
```

# Trabalho Futuro

- Checar o sistema operacional durante abertura do navegador e executar o comando correto ("sensible-browser' para Linux, "open" para MacOS e "???" para Windows)
- Implementar os métodos get, post, put e delete na classe SuapClient para poder executar as chamadas da API através cliente depois da autorização realizada método "authorize".
