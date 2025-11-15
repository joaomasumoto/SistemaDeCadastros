# Sistema de Cadastro CLI em Java

## Visão Geral
Aplicação de cadastro via terminal desenvolvida em Java. O sistema lê perguntas de um arquivo externo (`formulario.txt`), coleta dados de usuários, valida informações, persiste arquivos individuais e permite manutenção do formulário e busca avançada.

## Funcionalidades
- Leitura do formulário a partir de arquivo externo.
- Cadastro de usuário com validações:
  - Nome válido
  - Email válido e não duplicado
  - Idade > 18
  - Altura em formato numérico
- Persistência em arquivos `.TXT` sequenciais.
- Listagem de usuários cadastrados.
- Adição e exclusão de perguntas extras do formulário.
- Busca por nome, email ou idade.
- Reindexação automática das perguntas ao remover itens.
- Tratamento de exceções durante entrada e escrita de dados.

## Estrutura de Diretórios
```
/src
  /application
  /entities
  /services
  /util

/data
  formulario.txt
  sequence.txt
  <arquivos de usuários>.TXT
```

## Requisitos
- Java 17+

## Execução
Compilação:
```
javac -d bin src/**/*.java
```

Execução:
```
java -cp bin application.Program
```

## Arquivo `formulario.txt`
O arquivo deve ser criado manualmente pelo usuário na máquina local.

Conteúdo inicial obrigatório:
```
1 - Qual seu nome completo?
2 - Qual seu email de contato?
3 - Qual sua idade?
4 - Qual sua altura?
```

O caminho usado pelo sistema deve ser configurado em:
```
FileManager.java
```

Modifique a constante:
```
DIRECTORY_PATH = "<caminho local desejado>";
```

## Considerações
- O sistema não cria automaticamente o arquivo de formulário.
- O diretório configurado deve existir antes da execução.
- Funções de escrita reescrevem arquivos completos para garantir integridade.
