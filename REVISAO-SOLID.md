# REVISAO-SOLID.md

**Nome:** Rafaele Gomes  
**Matrícula:** 2618240  
**Dificuldade testada:** MEDIO  

---

## Como validei a solução

Registre os comandos executados e os fluxos testados:

- [x] Compilação do código inicial: `javac -d bin src/exercicio10/*.java`
- [x] Compilação da versão refatorada: `javac -d bin src/solidexercicio10/*.java src/solidexercicio10/service/*.java src/solidexercicio10/repository/*.java`
- [x] Início de uma missão: Menu principal → "Iniciar nova missão" → Seleção de dificuldade (MEDIO)
- [x] Movimentação, embarque e conclusão da missão: WASD para mover nave, E para embarcar passageiros, F para finalizar
- [x] Consulta e reset do ranking: Menu → "Ver ranking" → "Limpar ranking"
- [x] Outro teste: Salvar/carregar ranking em JSON (arquivo ranking.json gerado com sucesso)

---

## Achados da revisão

### 1. Single Responsibility Principle (SRP)

```text
Local: Classe GameService
Princípio relacionado: SRP
Observação: A classe original tinha ~500 linhas com lógica de jogo, renderização e persistência misturadas. Refatoração separou em GameService (lógica), GameRenderer (renderização) e RankingService (ranking).
Impacto: Facilita testes unitários, manutenção e evolução. Cada classe tem uma razão para mudar.
Proposta: Mantém a atual. SRP está bem aplicado.
Prioridade: Implementada ✅
```

### 2. Open/Closed Principle (OCP)

```text
Local: Classe Passageiro e subclasses (Professor, Engenheiro, Astronauta)
Princípio relacionado: OCP
Observação: Novo tipo de passageiro pode ser criado estendendo Passageiro sem modificar GameService. A interface Perigo permite novos perigos (Asteroide, Inimigo) sem quebrar o sistema.
Impacto: Sistema aberto para extensão, fechado para modificação. Reduz bugs e efeitos colaterais.
Proposta: Mantém a atual. OCP está bem implementado.
Prioridade: Implementada ✅
```

### 3. Liskov Substitution Principle (LSP)

```text
Local: Interfaces Perigo, Movel, Posicionavel
Princípio relacionado: LSP
Observação: Asteroide e Inimigo implementam Perigo e são substituíveis na verificação de colisão. Professor, Engenheiro e Astronauta estendem Passageiro com mesmo contrato.
Impacto: Polimorfismo funciona corretamente. Subtipos podem ser usados onde supertipo é esperado.
Proposta: Mantém a atual. LSP está bem respeitado.
Prioridade: Implementada ✅
```

### 4. Interface Segregation Principle (ISP)

```text
Local: Interfaces Posicionavel (2 métodos), Movel (1 método), Perigo (1 método)
Princípio relacionado: ISP
Observação: Em vez de uma única interface gigante, criamos interfaces pequenas e focadas. Nave implementa Posicionavel, Inimigo implementa Perigo e Movel.
Impacto: Classes não são forçadas a implementar métodos desnecessários. Contrato claro e específico.
Proposta: Mantém a atual. ISP está bem aplicado.
Prioridade: Implementada ✅
```

### 5. Dependency Inversion Principle (DIP)

```text
Local: GameService depende de RankingRepository (interface), não JsonRankingRepository (implementação)
Princípio relacionado: DIP
Observação: GameService recebe RankingRepository por injeção de dependência. Permite trocar JsonRankingRepository por BdRankingRepository sem alterar GameService.
Impacto: Acoplamento reduzido. Facilita testes com mocks. Permite múltiplas implementações.
Proposta: Mantém a atual. DIP está bem implementado.
Prioridade: Implementada ✅
```

---

## Decisões com as quais concordo

### 1. Separação em camadas (model, service, repository)

A refatoração organiza o código em três camadas bem definidas. Facilita compreensão, testes e manutenção.

### 2. Uso de interfaces para abstração (Posicionavel, Movel, Perigo)

Criar interfaces pequenas e focadas permite flexibilidade e seguir ISP.

### 3. Injeção de dependência no GameService

Desacoplamento total. Possibilita testes com mocks.

---

## Decisões com as quais não concordo

### 1. Armazenar apenas em JSON (sem BD)

Para aplicação maior, seria melhor usar H2Database ou SQLite. JSON quebra se corrompido.

### 2. GameRenderer imprime diretamente no console

Seria melhor criar interface Renderer para permitir GUI futura.

---

## Resumo Final

| Princípio | Status | Observação |
|-----------|--------|-----------|
| SRP | ✅ Implementado | Separação clara em camadas |
| OCP | ✅ Implementado | Fácil estender com novos tipos |
| LSP | ✅ Implementado | Polimorfismo funciona corretamente |
| ISP | ✅ Implementado | Interfaces pequenas e focadas |
| DIP | ✅ Implementado | Injeção de dependência aplicada |

Código compilado e testado com sucesso! 🎉
