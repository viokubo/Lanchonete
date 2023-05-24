import { Component } from '@angular/core';
import { PedidoService } from './pedido.service';

interface Pedido {
  id: number;
  itens: ItemPedido[];
  valorTotal: number;
}

interface ItemPedido {
  produtoId: number;
  quantidade: number;
  nome: string;
}

interface Produto {
  id: number;
  nome: string;
  preco: number;
}
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  msgErro!: string;
  pedidos!: Pedido[];
  produtos!: Produto[];
  produtoSelecionado!: number;
  quantidade!: number;
  pedidoSelecionado!: number;
  valorPagamento!: number;
  trocoMsg!: string;

  constructor(private pedidoService: PedidoService) {}

  criarPedido() {
    this.pedidoService.criarPedido().subscribe((pedidoId: number) => {
      const novoPedido: Pedido = { id: pedidoId, itens: [], valorTotal: 0 };
      this.pedidos.push(novoPedido);
    });
  }

  adicionarProduto(pedidoId: number) {
    this.pedidoService
      .adicionarProduto(pedidoId, this.produtoSelecionado, this.quantidade)
      .subscribe(() => {
        const pedidoAtualizado = this.pedidos.find(
          (pedido) => pedido.id === pedidoId
        );
        const itens = this.pedidos
          .filter((pedido) => {
            return pedido.id == pedidoAtualizado?.id;
          })[0]
          .itens.filter((produto) => {
            return produto.produtoId == this.produtoSelecionado;
          })[0];
        if (pedidoAtualizado && itens) {
          this.pedidos
            .filter((pedido) => {
              return pedido.id == pedidoAtualizado.id;
            })[0]
            .itens.filter((produto) => {
              return produto.produtoId == this.produtoSelecionado;
            })[0].quantidade = this.quantidade;
        } else {
          const item: ItemPedido = {
            produtoId: this.produtoSelecionado,
            quantidade: this.quantidade,
            nome: this.produtos.filter(
              (produto) => produto.id == this.produtoSelecionado
            )[0].nome,
          };
          pedidoAtualizado?.itens.push(item);
        }
      });
  }

  calcularValorPedido(pedidoId: number) {
    this.pedidoService
      .calcularPrecoTotal(pedidoId)
      .subscribe((valorTotal: number) => {
        const pedido = this.pedidos.filter(
          (pedido) => pedido.id === pedidoId
        )[0];
        pedido.valorTotal = valorTotal;
      });
    console.table(this.pedidos);
  }

  fecharPedido(pedidoId: number) {
    this.trocoMsg = '';
    this.pedidoSelecionado = pedidoId;
  }

  realizarPagamento(pedidoId: number) {
    this.pedidoService.fecharPedido(pedidoId, this.valorPagamento).subscribe(
      () => {
        this.msgErro = '';
        const index = this.pedidos.findIndex(
          (pedido) => pedido.id === pedidoId
        );

        const troco: number = Math.abs(
          this.pedidos[index].valorTotal - this.valorPagamento
        );
        if (troco > 0) {
          this.trocoMsg = 'Troco no valor de ' + troco;
        }

        if (index !== -1) {
          this.pedidos.splice(index, 1);
        }
      },
      (err) => {
        this.msgErro = err.error;
      }
    );
  }

  ngOnInit() {
    this.pedidoService.getProdutos().subscribe((produtos: Produto[]) => {
      this.produtos = produtos;
    });
    this.pedidos = [];
  }
}
