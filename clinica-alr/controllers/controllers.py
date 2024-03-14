# -*- coding: utf-8 -*-
# from odoo import http


# class Clinica-alr(http.Controller):
#     @http.route('/clinica-alr/clinica-alr', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/clinica-alr/clinica-alr/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('clinica-alr.listing', {
#             'root': '/clinica-alr/clinica-alr',
#             'objects': http.request.env['clinica-alr.clinica-alr'].search([]),
#         })

#     @http.route('/clinica-alr/clinica-alr/objects/<model("clinica-alr.clinica-alr"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('clinica-alr.object', {
#             'object': obj
#         })
