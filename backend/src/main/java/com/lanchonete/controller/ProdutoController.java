package com.lanchonete.controller;

import com.lanchonete.entities.Produto;
import com.lanchonete.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ProdutoController {

  private final ProdutoRepository produtoRepository;
  @Autowired
  public ProdutoController(ProdutoRepository produtoRepository) {
    this.produtoRepository = produtoRepository;
  }

  @GetMapping
  public List<Produto> getAllProdutos() {
    return produtoRepository.findAll();
  }
}
