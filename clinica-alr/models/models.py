# -*- coding: utf-8 -*-

import datetime
from odoo.exceptions import ValidationError
from odoo import models, fields, api

class Citas(models.Model):
    _name = 'clinica.citas'
    _description = 'Citas'

    name = fields.Char(string='Descripcion corta')
    fecha = fields.Date('Fecha')

    paciente_id = fields.Many2one('clinica.pacientes', string='Paciente', required=True, ondelete='cascade')

    @api.constrains('name')
    def _longitudName(self):
        for i in self:
            if len(i.name) >= 10:
                raise ValidationError('La descripcion debe contener menos de 10 caracteres')
    
class Pacientes(models.Model):
    _name = 'clinica.pacientes'
    _description = 'Pacientes'

    name = fields.Char(string='Nombre')
    fechaNacimiento = fields.Date(string='Fecha de Nacimiento')
    edad = fields.Integer(string='Edad', compute='_edad', store=True)
    numEnfermedades = fields.Integer(string='Numero de Enfermedades', compute='_numEnfermedades', store=True)

    enfermedades_ids = fields.Many2many('clinica.enfermedades', string='Enfermedad')
    citas_ids = fields.One2many('clinica.citas', 'paciente_id', string='Citas')

    @api.depends('fechaNacimiento')
    def _edad(self):
        for i in self:
            if i.fechaNacimiento:
                fechaActual = datetime.date.today()
                i.edad = fechaActual.year - i.fechaNacimiento.year
            else:
                i.edad = 0

    @api.depends('enfermedades_ids')
    def _numEnfermedades(self):
        for i in self:
            i.numEnfermedades = len(i.enfermedades_ids)
                
    @api.constrains('name')
    def _nombreUnico(self):
        existeNombre = self.search([('name', '=', self.name)])
        if existeNombre - self:
            raise ValidationError('El nombre debe ser unico')

class Enfermedades(models.Model):
    _name = 'clinica.enfermedades'
    _description = 'Enfermedades'

    name = fields.Char(string='Nombre')

    paciente_ids = fields.Many2many('clinica.pacientes', string='Paciente')