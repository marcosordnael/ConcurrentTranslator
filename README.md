# ConcurrentTranslator

Aplicativo Android para tradução de textos com suporte a seleção de idiomas, inversão rápida entre origem/destino, colar do clipboard e copiar o resultado traduzido.

## Funcionalidades

- Traduz texto usando uma API externa via RapidAPI
- Seleção de idioma de origem e destino
- Inverter idiomas com um toque
- Colar texto copiado diretamente no campo de entrada
- Copiar o resultado da tradução para a área de transferência
- Exibir mensagens claras de carregamento, sucesso e erro

## Tecnologias utilizadas

- Kotlin
- Android SDK
- View Binding
- ViewModel e LiveData
- Retrofit
- Kotlin Coroutines
- RapidAPI

## Como executar

1. Abra o projeto no Android Studio.
2. Configure sua chave da API no arquivo `local.properties` na raiz do projeto:

```properties
RAPID_API_KEY=sua_chave_aqui
RAPID_API_HOST=text-translator2.p.rapidapi.com
```

3. Sincronize o Gradle.
4. Execute o app em um emulador ou dispositivo físico com internet.

## Observações importantes

- O app depende de conexão com a internet para traduzir.
- Se o texto estiver vazio ou os idiomas forem iguais, o app mostra uma mensagem de validação.
- O resultado da tradução é exibido na tela e pode ser copiado com o ícone de copiar.

## Estrutura principal

- `app/src/main/java/com/marcos/concurrenttranslator/MainActivity.kt` — tela principal e ações de interface.
- `app/src/main/java/com/marcos/concurrenttranslator/ui/MainViewModel.kt` — regras de validação e estado da tradução.
- `app/src/main/java/com/marcos/concurrenttranslator/data/repository/TranslationRepository.kt` — chamada da API e tratamento de erros.
- `app/src/main/java/com/marcos/concurrenttranslator/data/api/TranslationApi.kt` — interface Retrofit da tradução.

## Fluxo da aplicação

1. O usuário digita ou cola um texto.
2. Escolhe os idiomas de origem e destino.
3. Toca em **Traduzir agora**.
4. O app envia a requisição para a API.
5. O resultado aparece no campo de saída e pode ser copiado.

## Logs úteis para teste

Durante o teste, você pode acompanhar os logs por estas tags:

- `MainViewModel`
- `TranslationRepository`

Esses logs ajudam a verificar:

- se a validação do texto foi aceita
- se a requisição foi enviada
- se a API respondeu corretamente
- se houve erro de rede, timeout ou resposta vazia

## Vídeo de demonstração

O vídeo de demonstração do aplicativo está disponível no link abaixo:
https://drive.google.com/file/d/1-2W0n3cv7S24LfiEJDrXLtqlIdlCeOPf/view?usp=sharing
